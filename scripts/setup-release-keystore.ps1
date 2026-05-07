#Requires -Version 5.0
<#
  One-time helper: creates release.keystore + keystore.properties at repo root.
  For plain-language instructions see docs/SIGNING_FOR_BEGINNERS.md
#>
$ErrorActionPreference = "Stop"

$RepoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
Set-Location $RepoRoot

function Find-Keytool {
    $jh = $env:JAVA_HOME
    if ($jh) {
        $candidates = @(
            (Join-Path $jh "bin\keytool.exe"),
            (Join-Path $jh "bin\keytool")
        )
        foreach ($p in $candidates) {
            if (Test-Path -LiteralPath $p) { return $p }
        }
    }
    $cmd = Get-Command keytool -ErrorAction SilentlyContinue
    if ($cmd) { return $cmd.Source }
    throw @"
Could not find keytool.

Fix: Install JDK 21 (or use Android Studio's bundled JDK), then either:
  - Set environment variable JAVA_HOME to that JDK folder, or
  - Add the JDK's bin folder to your PATH

Android Studio: Settings -> Build, Execution, Deployment -> Build Tools -> Gradle -> Gradle JDK (note the folder path and set JAVA_HOME to it).
"@
}

function Secure-ToPlain([System.Security.SecureString]$sec) {
    $bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($sec)
    try {
        [Runtime.InteropServices.Marshal]::PtrToStringAuto($bstr)
    }
    finally {
        [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr)
    }
}

Write-Host ""
Write-Host "=== Multi App Share: create release signing files (one-time) ===" -ForegroundColor Cyan
Write-Host ""
Write-Host "This will create:"
Write-Host "  - release.keystore     (your private signing key — BACK IT UP, never commit)"
Write-Host "  - keystore.properties  (passwords for Gradle on this PC — never commit)"
Write-Host ""
Write-Host "Working folder: $RepoRoot"
Write-Host ""

$keytool = Find-Keytool
Write-Host "Using: $keytool"
Write-Host ""

$ksPath = Join-Path $RepoRoot "release.keystore"
if (Test-Path -LiteralPath $ksPath) {
    Write-Host "STOP: This file already exists:" -ForegroundColor Red
    Write-Host "  $ksPath"
    Write-Host ""
    Write-Host "If you delete it and create a new one, you cannot replace existing installs"
    Write-Host "that were signed with the old key (Play Store / users would need a fresh install)."
    Write-Host ""
    exit 1
}

$aliasIn = Read-Host "Key alias [Enter = multiappshare]"
if ([string]::IsNullOrWhiteSpace($aliasIn)) { $aliasIn = "multiappshare" }

Write-Host ""
Write-Host "Choose a keystore password (save it in a password manager)."
$s1 = Read-Host "Keystore password" -AsSecureString
$s2 = Read-Host "Type the same password again" -AsSecureString
$storePass = Secure-ToPlain $s1
$confirm = Secure-ToPlain $s2
$s1.Dispose(); $s2.Dispose()
if ($storePass -ne $confirm) {
    Write-Host "Passwords did not match. Run the script again." -ForegroundColor Red
    exit 1
}

$same = Read-Host "Use the same password for the key inside the keystore? [Y/n]"
if ([string]::IsNullOrWhiteSpace($same) -or $same -match '^[yY]') {
    $keyPass = $storePass
}
else {
    $kp1 = Read-Host "Key password" -AsSecureString
    $kp2 = Read-Host "Confirm key password" -AsSecureString
    $keyPass = Secure-ToPlain $kp1
    $kcf = Secure-ToPlain $kp2
    $kp1.Dispose(); $kp2.Dispose()
    if ($keyPass -ne $kcf) {
        Write-Host "Key passwords did not match." -ForegroundColor Red
        exit 1
    }
}

Write-Host ""
$cn = Read-Host "Your name or organization (for certificate), e.g. Jane Doe"
if ([string]::IsNullOrWhiteSpace($cn)) { $cn = "Developer" }

# Minimal DN; CN is what users sometimes see in install prompts.
$dname = "CN=$cn, OU=Android, O=MultiAppShare, L=NA, ST=NA, C=US"

Write-Host ""
Write-Host "Creating keystore..."
& $keytool -genkeypair -v `
    -keystore $ksPath `
    -alias $aliasIn `
    -keyalg RSA -keysize 2048 -validity 10000 `
    -storepass $storePass `
    -keypass $keyPass `
    -dname $dname

if ($LASTEXITCODE -ne 0) {
    Write-Host "keytool failed with exit code $LASTEXITCODE" -ForegroundColor Red
    if (Test-Path -LiteralPath $ksPath) { Remove-Item -LiteralPath $ksPath -Force }
    exit $LASTEXITCODE
}

$propsPath = Join-Path $RepoRoot "keystore.properties"
$nl = [Environment]::NewLine
$body = "storeFile=release.keystore${nl}storePassword=$storePass${nl}keyAlias=$aliasIn${nl}keyPassword=$keyPass${nl}"
# UTF-8 without BOM for cleaner Gradle/Java properties reads on Windows
$utf8NoBom = New-Object System.Text.UTF8Encoding $false
[System.IO.File]::WriteAllText($propsPath, $body, $utf8NoBom)

Write-Host ""
Write-Host "Done." -ForegroundColor Green
Write-Host "  Keystore : $ksPath"
Write-Host "  Gradle config (secret): $propsPath"
Write-Host ""
Write-Host "Next steps:"
Write-Host "  1. Back up release.keystore somewhere safe (loss = you cannot ship updates with this identity)."
Write-Host "  2. Build: .\scripts\build-signed-release-apk.ps1"
Write-Host "  3. Read docs\SIGNING_FOR_BEGINNERS.md for adb install and releases."
Write-Host ""
