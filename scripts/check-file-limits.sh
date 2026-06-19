#!/usr/bin/env bash
# File line limits — delegates to check-file-limits.ps1 (MultiAppShare module paths).
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"

if command -v pwsh >/dev/null 2>&1; then
  pwsh "$ROOT/scripts/check-file-limits.ps1" -Fail
elif command -v powershell >/dev/null 2>&1; then
  powershell -NoProfile -ExecutionPolicy Bypass -File "$ROOT/scripts/check-file-limits.ps1" -Fail
else
  echo "WARN: pwsh not found; skipping check-file-limits"
  exit 0
fi
