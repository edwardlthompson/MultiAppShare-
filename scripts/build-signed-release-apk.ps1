#Requires -Version 5.0
<#
  Builds a signed :app release APK when keystore.properties or RELEASE_* env vars are set.

  New to signing? Run .\scripts\setup-release-keystore.ps1 once, then read docs\SIGNING_FOR_BEGINNERS.md
  Technical details: docs/LOCAL_RELEASE_BUILD.md
#>
$ErrorActionPreference = "Stop"
$root = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
Set-Location $root

$hasFile = Test-Path (Join-Path $root "keystore.properties")
$hasEnv = $env:RELEASE_KEYSTORE_PATH -or $env:RELEASE_KEYSTORE_PASSWORD
if (-not $hasFile -and -not $hasEnv) {
    Write-Warning "No keystore.properties and no RELEASE_* env vars. Copy keystore.properties.example -> keystore.properties, or set env (see docs/LOCAL_RELEASE_BUILD.md). Assemble will still run; release may be unsigned without a keystore."
}

& (Join-Path $root "gradlew.bat") ":app:assembleRelease" @args
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

$apkDir = Join-Path $root "app\build\outputs\apk\release"
if (Test-Path $apkDir) {
    Get-ChildItem -Path $apkDir -Filter "*.apk" | ForEach-Object {
        Write-Host "APK: $($_.FullName)"
    }
}
