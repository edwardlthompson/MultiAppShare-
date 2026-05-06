#!/usr/bin/env pwsh
# dump-full-logs.ps1
param([string]$RunId)
$Token = (Get-Content "C:\Users\edwar\MultiAppShare-\scripts\.env.local" | Select-String "GITHUB_TOKEN=(.+)" ).Matches.Groups[1].Value.Trim()
$headers = @{ Authorization = "Bearer $Token"; Accept = "application/vnd.github+json" }
$base = "https://api.github.com/repos/edwardlthompson/MultiAppShare-"
$jobs = (Invoke-RestMethod "$base/actions/runs/$RunId/jobs" -Headers $headers).jobs
$failedJob = $jobs | Where-Object { $_.conclusion -eq "failure" } | Select-Object -First 1
if ($failedJob) {
    Write-Host "Downloading logs for job $($failedJob.id)..."
    Invoke-WebRequest "$base/actions/jobs/$($failedJob.id)/logs" -Headers $headers -OutFile "C:\Users\edwar\MultiAppShare-\full_ci_log.txt"
    Write-Host "Done. Saved to full_ci_log.txt"
}
