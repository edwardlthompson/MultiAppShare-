#!/usr/bin/env pwsh
# get-ci-logs.ps1
# Downloads and prints the log of the latest FAILED GitHub Actions run.
# Usage: .\scripts\get-ci-logs.ps1

param(
    [string]$Token = $env:GITHUB_TOKEN,
    [string]$Owner = "edwardlthompson",
    [string]$Repo  = "MultiAppShare-",
    [int]   $TopN  = 3
)

# Load token from .env.local
if (-not $Token) {
    $envFile = Join-Path $PSScriptRoot ".env.local"
    if (Test-Path $envFile) {
        Get-Content $envFile | ForEach-Object {
            if ($_ -match "^\s*GITHUB_TOKEN\s*=\s*`"?(.*?)`"?\s*$") { 
                $Token = $Matches[1].Trim() 
            }
        }
    }
}

if (-not $Token -or $Token -eq "ghp_paste_your_token_here") {
    Write-Host "Error: GITHUB_TOKEN not found in scripts/.env.local" -ForegroundColor Red
    exit 1
}

$headers = @{
    Authorization = "Bearer $Token"
    Accept        = "application/vnd.github+json"
    "X-GitHub-Api-Version" = "2022-11-28"
}
$base = "https://api.github.com/repos/$Owner/$Repo"

Write-Host "`n=== Recent Runs ===" -ForegroundColor Yellow
try {
    $runsResponse = Invoke-RestMethod "$base/actions/runs?per_page=10" -Headers $headers
    $runs = $runsResponse.workflow_runs | Select-Object -First $TopN
} catch {
    Write-Error "Failed to fetch runs: $($_.Exception.Message)"
    exit 1
}

foreach ($run in $runs) {
    $color = if ($run.conclusion -eq "success") { "Green" } elseif ($run.conclusion -eq "failure") { "Red" } else { "Gray" }
    Write-Host "`n--- Run #$($run.run_number): $($run.name) ($($run.head_sha.Substring(0,7))) -> $($run.conclusion) ---" -ForegroundColor $color

    if ($run.conclusion -eq "failure") {
        $jobsResponse = Invoke-RestMethod "$base/actions/runs/$($run.id)/jobs" -Headers $headers
        $failedJob = $jobsResponse.jobs | Where-Object { $_.conclusion -eq "failure" } | Select-Object -First 1
        if ($failedJob) {
            Write-Host "  Downloading logs for failing job: $($failedJob.name)..." -ForegroundColor Yellow
            $logUrl = "$base/actions/jobs/$($failedJob.id)/logs"
            $tmpFile = [System.IO.Path]::GetTempFileName()
            try {
                Invoke-WebRequest $logUrl -Headers $headers -OutFile $tmpFile
                $lines = Get-Content $tmpFile
                Write-Host "  === Last 20 lines of failure ===" -ForegroundColor Red
                $lines | Select-Object -Last 20 | ForEach-Object { Write-Host "  $_" -ForegroundColor Red }
            } finally {
                if (Test-Path $tmpFile) { Remove-Item $tmpFile }
            }
        }
    }
}
Write-Host "`nDone." -ForegroundColor Green
