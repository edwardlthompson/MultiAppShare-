param(
    [int]$ViewLimit = 250,
    [int]$LogicLimit = 150,
    [switch]$Fail
)

$ErrorActionPreference = "Stop"
$root = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
$mainJava = Join-Path $root "app\src\main\java"

$viewPatterns = @(
    "*Screen*.kt",
    "*Ui*.kt",
    "*Dialog*.kt",
    "*Composable*",
    "ui\**\*.kt"
)
$logicPatterns = @(
    "*ViewModel*.kt",
    "*Repository*.kt",
    "*UseCase*.kt",
    "*Cipher*.kt"
)

function Test-FileLimit {
    param([string]$Path, [int]$Limit, [string]$Kind)
    $lines = (Get-Content $Path | Measure-Object -Line).Lines
    if ($lines -gt $Limit) {
        Write-Host "[$Kind] $Path : $lines lines (limit $Limit)"
        return $true
    }
    return $false
}

$violations = $false

Get-ChildItem -Path $mainJava -Recurse -Filter "*.kt" | ForEach-Object {
    $rel = $_.FullName.Substring($mainJava.Length + 1)
    $isView = $false
    foreach ($p in $viewPatterns) {
        if ($rel -like $p -or $_.Name -like $p) { $isView = $true; break }
    }
    $isLogic = $false
    foreach ($p in $logicPatterns) {
        if ($rel -like $p -or $_.Name -like $p) { $isLogic = $true; break }
    }

    if ($isView) {
        if (Test-FileLimit $_.FullName $ViewLimit "view") { $violations = $true }
    } elseif ($isLogic) {
        if (Test-FileLimit $_.FullName $LogicLimit "logic") { $violations = $true }
    }
}

if ($violations) {
    if ($Fail) { exit 1 } else { Write-Host "File size warnings present (warn-only)." ; exit 0 }
} else {
    Write-Host "All checked files within limits."
    exit 0
}
