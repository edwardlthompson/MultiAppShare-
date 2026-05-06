#!/usr/bin/env pwsh
# sync-fdroiddata-gitlab.ps1
# Syncs canonical F-Droid metadata from this repo to your GitLab fdroiddata fork, pushes, and opens (or reuses) an MR to fdroid/fdroiddata.
#
# Prerequisites:
#   - GitLab Personal Access Token with at least: api, write_repository
#   - git available on PATH
#
# Token: set GITLAB_TOKEN in the environment, or add GITLAB_TOKEN=... to scripts/.env.local (same pattern as get-ci-logs.ps1).
#
# Usage:
#   .\scripts\sync-fdroiddata-gitlab.ps1
#   .\scripts\sync-fdroiddata-gitlab.ps1 -Tag v1.7.4 -SkipMr
#   .\scripts\sync-fdroiddata-gitlab.ps1 -FdroidDataPath D:\src\fdroiddata

param(
    [string]$Token = $env:GITLAB_TOKEN,

    # Your fork: namespace/project on gitlab.com
    [string]$GitLabForkPath = "edwardleethompson/fdroiddata",

    # Upstream F-Droid data repo (MR target)
    [string]$UpstreamPath = "fdroid/fdroiddata",

    [string]$GitLabHost = "https://gitlab.com",

    # Sibling folder ../fdroiddata when omitted (relative to parent of this app repo)
    [string]$FdroidDataPath = "",

    [string]$SourceBranch = "master",
    [string]$TargetBranch = "master",

    [string]$Tag = "",
    [string]$VersionName = "",
    [int]$VersionCode = 0,

    [string]$ApplicationId = "com.edwardlthompson.multiappshare",

    [switch]$SkipMr,
    [switch]$DryRun
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Read-Token {
    param([string]$Initial)
    if ($Initial) { return $Initial }
    $envFile = Join-Path $PSScriptRoot ".env.local"
    if (Test-Path $envFile) {
        Get-Content $envFile | ForEach-Object {
            if ($_ -match '^\s*GITLAB_TOKEN\s*=\s*"?([^"]*)"?') {
                return $Matches[1].Trim()
            }
        }
    }
    return $null
}

function Get-ProjectId {
    param(
        [string]$HostUrl,
        [string]$ProjectPath,
        [hashtable]$Headers
    )
    $enc = [uri]::EscapeDataString($ProjectPath)
    $uri = "$HostUrl/api/v4/projects/$enc"
    return (Invoke-RestMethod -Uri $uri -Headers $Headers -Method Get).id
}

function Update-FdroidMetadataContent {
    param(
        [string]$Content,
        [string]$VerName,
        [int]$VerCode,
        [string]$GitTag
    )
    $nl = [Environment]::NewLine
    $lines = $Content -split "\r?\n"
    $out = foreach ($line in $lines) {
        if ($line -match '^\s+versionName:\s') {
            "    versionName: $VerName"
        }
        elseif ($line -match '^\s+versionCode:\s') {
            "    versionCode: $VerCode"
        }
        elseif ($line -match '^\s+commit:\s') {
            "    commit: $GitTag"
        }
        elseif ($line -match '^CurrentVersion:\s') {
            "CurrentVersion: $VerName"
        }
        elseif ($line -match '^CurrentVersionName:\s') {
            "CurrentVersion: $VerName"
        }
        elseif ($line -match '^CurrentVersionCode:\s') {
            "CurrentVersionCode: $VerCode"
        }
        else {
            $line
        }
    }
    return ($out -join $nl)
}

function Read-VersionsFromGradle {
    param([string]$GradlePath)
    $text = Get-Content -LiteralPath $GradlePath -Raw
    $vn = if ($text -match 'versionName\s*=\s*"([^"]+)"') { $Matches[1] } else { $null }
    $vc = if ($text -match 'versionCode\s*=\s*(\d+)') { [int]$Matches[1] } else { $null }
    if (-not $vn -or -not $vc) {
        throw "Could not parse versionName/versionCode from $GradlePath"
    }
    return @{ VersionName = $vn; VersionCode = $vc }
}

# --- main ---
$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
$gradlePath = Join-Path $repoRoot "app/build.gradle.kts"
$canonicalMeta = Join-Path $repoRoot "metadata/$ApplicationId.yml"

if (-not (Test-Path -LiteralPath $canonicalMeta)) {
    throw "Missing canonical metadata: $canonicalMeta"
}

$gv = Read-VersionsFromGradle -GradlePath $gradlePath
if (-not $VersionName) { $VersionName = $gv.VersionName }
if ($VersionCode -le 0) { $VersionCode = $gv.VersionCode }
if (-not $Tag) { $Tag = "v$VersionName" }

if (-not $FdroidDataPath) {
    $parentDir = Split-Path $repoRoot -Parent
    $FdroidDataPath = Join-Path $parentDir "fdroiddata"
}

$metaContent = Get-Content -LiteralPath $canonicalMeta -Raw
$metaContent = Update-FdroidMetadataContent -Content $metaContent -VerName $VersionName -VerCode $VersionCode -GitTag $Tag
$destMeta = Join-Path $FdroidDataPath "metadata/$ApplicationId.yml"

if ($DryRun) {
    Write-Host "App repo:        $repoRoot" -ForegroundColor Cyan
    Write-Host "F-Droid data:    $FdroidDataPath" -ForegroundColor Cyan
    Write-Host "Would write:     $destMeta" -ForegroundColor Cyan
    Write-Host "Versions:        $VersionName ($VersionCode), tag $Tag" -ForegroundColor Cyan
    Write-Host "`n[DryRun] Metadata preview (first 35 lines):" -ForegroundColor Yellow
    ($metaContent -split "`n" | Select-Object -First 35) | ForEach-Object { Write-Host $_ }
    exit 0
}

$Token = Read-Token -Initial $Token
if (-not $Token) {
    Write-Host "Error: Set GITLAB_TOKEN or add it to scripts/.env.local" -ForegroundColor Red
    exit 1
}

Write-Host "App repo:        $repoRoot" -ForegroundColor Cyan
Write-Host "F-Droid data:    $FdroidDataPath" -ForegroundColor Cyan
Write-Host "Versions:        $VersionName ($VersionCode), tag $Tag" -ForegroundColor Cyan
Write-Host "GitLab fork:     $GitLabForkPath" -ForegroundColor Cyan

$glUri = [uri]$GitLabHost
$tokEsc = [uri]::EscapeDataString($Token)
$cloneUrl = "https://oauth2:${tokEsc}@$($glUri.Host)/$GitLabForkPath.git"
if (-not (Test-Path -LiteralPath $FdroidDataPath)) {
    Write-Host "Cloning $GitLabForkPath -> $FdroidDataPath" -ForegroundColor Yellow
    $parentFd = Split-Path $FdroidDataPath -Parent
    if (-not (Test-Path $parentFd)) { New-Item -ItemType Directory -Path $parentFd | Out-Null }
    git clone $cloneUrl $FdroidDataPath
    if ($LASTEXITCODE -ne 0) { throw "git clone failed" }
}

Push-Location $FdroidDataPath
try {
    $prevOrigin = ""
    try { $prevOrigin = (git remote get-url origin 2>$null) } catch { }
    git remote set-url origin $cloneUrl

    git checkout $SourceBranch
    if ($LASTEXITCODE -ne 0) { throw "git checkout $SourceBranch failed" }
    git pull origin $SourceBranch
    if ($LASTEXITCODE -ne 0) { throw "git pull failed" }

    $destDir = Split-Path $destMeta -Parent
    if (-not (Test-Path $destDir)) {
        New-Item -ItemType Directory -Path $destDir | Out-Null
    }
    $utf8NoBom = New-Object System.Text.UTF8Encoding $false
    [System.IO.File]::WriteAllText($destMeta, $metaContent, $utf8NoBom)

    git add -- "metadata/$ApplicationId.yml"
    $status = git status --porcelain
    if (-not $status) {
        Write-Host "No metadata changes; nothing to commit." -ForegroundColor Green
    }
    else {
        git commit -m "metadata: $ApplicationId $Tag ($VersionName / $VersionCode)"
        if ($LASTEXITCODE -ne 0) { throw "git commit failed" }
    }

    git push origin $SourceBranch
    if ($LASTEXITCODE -ne 0) { throw "git push failed" }
    Write-Host "Pushed $SourceBranch to $GitLabForkPath" -ForegroundColor Green
}
finally {
    if ($prevOrigin) {
        git remote set-url origin $prevOrigin
    }
    Pop-Location
}

if ($SkipMr) {
    Write-Host "SkipMr: not creating merge request." -ForegroundColor Yellow
    exit 0
}

$headers = @{ "PRIVATE-TOKEN" = $Token }
$forkId = Get-ProjectId -HostUrl $GitLabHost -ProjectPath $GitLabForkPath -Headers $headers
$upstreamId = Get-ProjectId -HostUrl $GitLabHost -ProjectPath $UpstreamPath -Headers $headers

function Get-ExistingMergeRequest {
    param([int]$UpstreamProjectId, [int]$ForkProjectId, [string]$SrcBranch, [string]$TgtBranch)
    # Precise query (avoids missing MRs past the first page of all open MRs)
    $q = "state=opened&source_branch=$([uri]::EscapeDataString($SrcBranch))&source_project_id=$ForkProjectId&target_branch=$([uri]::EscapeDataString($TgtBranch))&per_page=20"
    try {
        $list = Invoke-RestMethod `
            -Uri "$GitLabHost/api/v4/projects/$UpstreamProjectId/merge_requests?$q" `
            -Headers $headers -Method Get
        return $list | Select-Object -First 1
    }
    catch {
        return $null
    }
}

$mr = Get-ExistingMergeRequest -UpstreamProjectId $upstreamId -ForkProjectId $forkId `
    -SrcBranch $SourceBranch -TgtBranch $TargetBranch

if (-not $mr) {
    $mrPage = Invoke-RestMethod `
        -Uri "$GitLabHost/api/v4/projects/$upstreamId/merge_requests?state=opened&per_page=100" `
        -Headers $headers -Method Get
    $mr = $mrPage | Where-Object {
        $_.source_project_id -eq $forkId -and
        $_.source_branch -eq $SourceBranch -and
        $_.target_branch -eq $TargetBranch
    } | Select-Object -First 1
}

if (-not $mr) {
    $mrPage = Invoke-RestMethod `
        -Uri "$GitLabHost/api/v4/projects/$upstreamId/merge_requests?state=opened&per_page=100" `
        -Headers $headers -Method Get
    $mr = $mrPage | Where-Object {
        $_.source_project_id -eq $forkId -and
        ($_.title -like "*$ApplicationId*" -or $_.description -like "*$ApplicationId*")
    } | Select-Object -First 1
}

if ($mr) {
    Write-Host "Open MR already exists: $($mr.web_url)" -ForegroundColor Green
    exit 0
}

$body = @{
    source_branch       = $SourceBranch
    target_branch       = $TargetBranch
    title               = "${ApplicationId}: sync metadata for $Tag"
    description         = "Automated metadata sync from GitHub `edwardlthompson/MultiAppShare-` (tag **$Tag**, version **$VersionName** / **$VersionCode**)."
    target_project_id   = $upstreamId
    remove_source_branch = $false
}

try {
    $created = Invoke-RestMethod `
        -Uri "$GitLabHost/api/v4/projects/$forkId/merge_requests" `
        -Headers $headers `
        -Method Post `
        -ContentType "application/json" `
        -Body ($body | ConvertTo-Json)
    Write-Host "Created MR: $($created.web_url)" -ForegroundColor Green
}
catch {
    $errRaw = $_.ErrorDetails.Message
    if ($errRaw -match '!(\d+)') {
        $iid = [int]$Matches[1]
        $dupUrl = "$GitLabHost/$UpstreamPath/-/merge_requests/$iid"
        Write-Host "GitLab says an MR already exists for this branch — open it here:" -ForegroundColor Yellow
        Write-Host $dupUrl -ForegroundColor Green
        Write-Host "(Your metadata push may still have succeeded; refresh that MR to see the latest pipeline.)" -ForegroundColor Cyan
        exit 0
    }
    if ($errRaw) {
        Write-Host "MR create response: $errRaw" -ForegroundColor Yellow
    }
    Write-Error "Merge request creation failed (or token lacks api scope): $($_.Exception.Message)"
    exit 1
}
