#!/usr/bin/env pwsh
# get-ci-logs.ps1
# Downloads and prints the log of the latest FAILED GitHub Actions run.
# Usage: .\scripts\get-ci-logs.ps1

param(
    [string]$Token = $env:GITHUB_TOKEN,
    [string]$Owner = "edwardlthompson",
    [string]$Repo  = "MultiAppShare-",
    [int]   $TopN  = 3      # how many recent failed runs to show
)

# Auto-load token from scripts/.env.local if not already set
if (-not $Token) {
    $envFile = Join-Path $PSScriptRoot ".env.local"
    if (Test-Path $envFile) {
        Get-Content $envFile | ForEach-Object {
            # Matches GITHUB_TOKEN = "val", GITHUB_TOKEN=val, etc.
            if ($_ -match "^\s*GITHUB_TOKEN\s*=\s*`"?(.*?)`"?\s*$") { 
                $Token = $Matches[1].Trim() 
            }
        }
    }
}

if (-not $Token -or $Token -eq "ghp_paste_your_token_here") {
    Write-Host "Error: GITHUB_TOKEN not found or still set to placeholder in scripts/.env.local" -ForegroundColor Red
    Write-Host "Please edit C:\Users\edwar\MultiAppShare-\scripts\.env.local and paste your real token." -ForegroundColor Yellow
    exit 1
}

$headers = @{
    Authorization = "Bearer $Token"
    Accept        = "application/vnd.github+json"
    "X-GitHub-Api-Version" = "2022-11-28"
}
$base = "https://api.github.com/repos/$Owner/$Repo"

# --- 1. Get recent failed runs ---
Write-Host "`n=== Recent FAILED runs ===" -ForegroundColor Yellow
try {
    $runsResponse = Invoke-RestMethod "$base/actions/runs?per_page=20" -Headers $headers
    $runs = $runsResponse.workflow_runs |
            Where-Object { $_.conclusion -eq "failure" } |
            Select-Object -First $TopN
} catch {
    Write-Error "Failed to fetch runs: $($_.Exception.Message)"
    exit 1
}

if (-not $runs) {
    Write-Host "No failed runs found in the last 20 attempts." -ForegroundColor Gray
    exit 0
}

foreach ($run in $runs) {
    $dur = [int]([datetime]$run.updated_at - [datetime]$run.created_at).TotalSeconds
    Write-Host "`n--- Run #$($run.run_number): $($run.name) ($($run.head_sha.Substring(0,7))) [${dur}s] ---" -ForegroundColor Cyan

    # --- 2. Get step timings for this run ---
    $jobsResponse = Invoke-RestMethod "$base/actions/runs/$($run.id)/jobs" -Headers $headers
    $jobs = $jobsResponse.jobs
    foreach ($job in $jobs) {
        Write-Host "  Job: $($job.name) -> $($job.conclusion)"
        foreach ($step in $job.steps) {
            $icon = if ($step.conclusion -eq "failure") { "FAIL" } elseif ($step.conclusion -eq "success") { "ok  " } else { "skip" }
            $stepDur = if ($step.started_at -and $step.completed_at) {
                [int]([datetime]$step.completed_at - [datetime]$step.started_at).TotalSeconds
            } else { 0 }
            $color = if ($step.conclusion -eq "failure") { "Red" } else { "Gray" }
            Write-Host "    [$icon] ($($stepDur)s) $($step.name)" -ForegroundColor $color
        }
    }

    # --- 3. Download the raw log zip for the first failed job ---
    $failedJob = $jobs | Where-Object { $_.conclusion -eq "failure" } | Select-Object -First 1
    if ($failedJob) {
        Write-Host "`n  Downloading logs for failed job: $($failedJob.name)..." -ForegroundColor Yellow
        $logUrl  = "$base/actions/jobs/$($failedJob.id)/logs"
        $tmpFile = [System.IO.Path]::GetTempFileName() + ".txt"
        try {
            Invoke-WebRequest $logUrl -Headers $headers -OutFile $tmpFile
            $lines = Get-Content $tmpFile
            
            # Find the line indices of failing tasks or errors
            $failureIndices = @()
            for ($i = 0; $i -lt $lines.Count; $i++) {
                if ($lines[$i] -match "FAILED|error:|exception|unresolved|could not|BUILD FAILED|:app:|:core-") {
                    $failureIndices += $i
                }
            }

            if ($failureIndices.Count -gt 0) {
                Write-Host "`n  === Detailed Error Context ===" -ForegroundColor Red
                # Get unique lines with context (10 before, 5 after)
                $shownIndices = @{}
                foreach ($idx in $failureIndices) {
                    $start = [Math]::Max(0, $idx - 15)
                    $end = [Math]::Min($lines.Count - 1, $idx + 5)
                    for ($j = $start; $j -le $end; $j++) {
                        if (-not $shownIndices.ContainsKey($j)) {
                            $lColor = if ($lines[$j] -match "FAILED|error:|exception") { "Red" } else { "Gray" }
                            Write-Host "  [$j] $($lines[$j])" -ForegroundColor $lColor
                            $shownIndices[$j] = $true
                        }
                    }
                }
            } else {
                Write-Host "`n  === Last 100 log lines (no obvious errors found) ===" -ForegroundColor Gray
                $lines | Select-Object -Last 100 | ForEach-Object { Write-Host "  $_" }
            }
        } catch {
            Write-Host "  Could not download log: $_" -ForegroundColor DarkYellow
        } finally {
            if (Test-Path $tmpFile) { Remove-Item $tmpFile -ErrorAction SilentlyContinue }
        }
    }
}

Write-Host "`nDone." -ForegroundColor Green
