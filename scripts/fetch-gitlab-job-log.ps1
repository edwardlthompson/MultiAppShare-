#!/usr/bin/env pwsh
# fetch-gitlab-job-log.ps1
# Fetches CI job logs from GitLab using the REST API (works on Free tier; needs a Personal Access Token).
#
# Token: GITLAB_TOKEN in environment or scripts/.env.local (same as sync-fdroiddata-gitlab.ps1).
# PAT scopes: read_api (minimum). If listing pipelines/jobs fails, add read_repository.
#
# Examples:
#   .\scripts\fetch-gitlab-job-log.ps1 -JobUrl "https://gitlab.com/edwardleethompson/fdroiddata/-/jobs/14250553043"
#   .\scripts\fetch-gitlab-job-log.ps1 -JobId 14250553043 -ProjectPath "edwardleethompson/fdroiddata"
#   .\scripts\fetch-gitlab-job-log.ps1 -PipelineUrl "https://gitlab.com/edwardleethompson/fdroiddata/-/pipelines/2505809084"
#   .\scripts\fetch-gitlab-job-log.ps1 -PipelineUrl "..." -FailedOnly
#   .\scripts\fetch-gitlab-job-log.ps1 -JobUrl "..." -OutFile ".\job-log.txt"

param(
    [string]$Token = $env:GITLAB_TOKEN,

    [string]$GitLabHost = "https://gitlab.com",

    # project path with slash, e.g. edwardleethompson/fdroiddata
    [string]$ProjectPath = "edwardleethompson/fdroiddata",

    [string]$JobUrl = "",
    [string]$PipelineUrl = "",

    [string]$JobId = "",
    [string]$PipelineId = "",

    [switch]$FailedOnly,
    [string]$OutFile = ""
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Read-GitLabToken {
    param([string]$Initial)
    if ($Initial) { return $Initial.Trim() }
    $envFile = Join-Path $PSScriptRoot ".env.local"
    if (Test-Path $envFile) {
        foreach ($line in Get-Content $envFile) {
            if ($line -match '^\s*GITLAB_TOKEN\s*=\s*"?([^"#]+)"?\s*(?:#.*)?$') {
                return $Matches[1].Trim()
            }
        }
    }
    return $null
}

function Resolve-GitLabUri {
    param([string]$Url, [string]$Kind)
    $u = [Uri]$Url
    $hostName = $u.Host
    $path = $u.AbsolutePath
    if ($Kind -eq 'job' -and $path -match '^/(.+)/-/jobs/(\d+)') {
        return @{
            Host          = $hostName
            ProjectPath   = $Matches[1]
            JobId         = $Matches[2]
        }
    }
    if ($Kind -eq 'pipeline' -and $path -match '^/(.+)/-/pipelines/(\d+)') {
        return @{
            Host          = $hostName
            ProjectPath   = $Matches[1]
            PipelineId    = $Matches[2]
        }
    }
    return $null
}

function Get-ApiBase {
    param([string]$HostName)
    if ($HostName -match '^https?://') {
        $h = ([Uri]$HostName).GetLeftPart([UriPartial]::Authority)
        return "$h/api/v4"
    }
    return "https://$HostName/api/v4"
}

$Token = Read-GitLabToken -Initial $Token
if (-not $Token) {
    Write-Host "Error: Set GITLAB_TOKEN or add GITLAB_TOKEN=... to scripts/.env.local (copy from scripts/.env.local.example)." -ForegroundColor Red
    exit 1
}

if ($JobUrl) {
    $r = Resolve-GitLabUri -Url $JobUrl -Kind 'job'
    if (-not $r) {
        throw "Could not parse JobUrl (expect .../namespace/project/-/jobs/NUMBER)"
    }
    $ProjectPath = $r.ProjectPath
    $JobId = $r.JobId
    $GitLabHost = "https://$($r.Host)"
}

if ($PipelineUrl) {
    $r = Resolve-GitLabUri -Url $PipelineUrl -Kind 'pipeline'
    if (-not $r) {
        throw "Could not parse PipelineUrl (expect .../namespace/project/-/pipelines/NUMBER)"
    }
    $ProjectPath = $r.ProjectPath
    $PipelineId = $r.PipelineId
    $GitLabHost = "https://$($r.Host)"
}

$apiBase = Get-ApiBase -HostName $GitLabHost
$projEnc = [uri]::EscapeDataString($ProjectPath)
$headers = @{ "PRIVATE-TOKEN" = $Token }

if ($PipelineId -and -not $JobId) {
    $jobsUri = "$apiBase/projects/$projEnc/pipelines/$PipelineId/jobs?per_page=100"
    Write-Host "GET $jobsUri" -ForegroundColor DarkGray
    try {
        $jobs = Invoke-RestMethod -Uri $jobsUri -Headers $headers -Method Get
    }
    catch {
        Write-Error "Failed to list pipeline jobs: $($_.Exception.Message)"
        exit 1
    }
    if ($FailedOnly) {
        $jobs = $jobs | Where-Object { $_.status -eq 'failed' }
    }
    Write-Host "`nPipeline $PipelineId jobs ($($jobs.Count) shown):" -ForegroundColor Cyan
    $jobs | ForEach-Object {
        $icon = switch ($_.status) {
            'failed' { 'FAIL' }
            'success' { ' ok ' }
            'skipped' { 'skip' }
            default { $_.status }
        }
        Write-Host ("[{0}] id={1} stage={2} name={3}" -f $icon, $_.id, $_.stage, $_.name)
        Write-Host ("      web: {0}" -f $_.web_url)
    }
    Write-Host "`nFetch a log with:" -ForegroundColor Yellow
    Write-Host "  .\scripts\fetch-gitlab-job-log.ps1 -JobId <id> -ProjectPath `"$ProjectPath`" -GitLabHost `"$GitLabHost`""
    exit 0
}

if ($JobId -and $JobId -notmatch '^\d+$') {
    throw "JobId must be numeric (digits only)."
}

if (-not $JobId) {
    Write-Host @"
Usage:
  List jobs in a pipeline:
    .\scripts\fetch-gitlab-job-log.ps1 -PipelineUrl `"https://gitlab.com/NAMESPACE/PROJECT/-/pipelines/PIPELINE_ID`"
    .\scripts\fetch-gitlab-job-log.ps1 -PipelineUrl `"...`" -FailedOnly

  Download one job log:
    .\scripts\fetch-gitlab-job-log.ps1 -JobUrl `"https://gitlab.com/NAMESPACE/PROJECT/-/jobs/JOB_ID`"
    .\scripts\fetch-gitlab-job-log.ps1 -JobId JOB_ID -ProjectPath `"NAMESPACE/PROJECT`"

Optional: -OutFile path\to\log.txt   (-GitLabHost for self-hosted GitLab)
"@ -ForegroundColor Gray
    exit 1
}

$traceUri = "$apiBase/projects/$projEnc/jobs/$JobId/trace"
Write-Host "GET $traceUri" -ForegroundColor DarkGray

try {
    $resp = Invoke-WebRequest -Uri $traceUri -Headers $headers -Method Get -UseBasicParsing
}
catch {
    Write-Error "Failed to fetch job trace (check JobId, project path, token scopes): $($_.Exception.Message)"
    exit 1
}

$body = $resp.Content

if ($OutFile) {
    $resolved = $ExecutionContext.SessionState.Path.GetUnresolvedProviderPathFromPSPath($OutFile)
    $parent = Split-Path $resolved -Parent
    if ($parent -and -not (Test-Path -LiteralPath $parent)) {
        New-Item -ItemType Directory -Path $parent -Force | Out-Null
    }
    $utf8NoBom = New-Object System.Text.UTF8Encoding $false
    [System.IO.File]::WriteAllText($resolved, $body, $utf8NoBom)
    Write-Host "`nWrote $($body.Length) characters to $resolved" -ForegroundColor Green
}
else {
    Write-Host ""
    Write-Host $body
}
