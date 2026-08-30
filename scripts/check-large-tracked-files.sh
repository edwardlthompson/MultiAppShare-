#!/usr/bin/env bash
# Fail if any tracked file exceeds size budget (matches pre-commit 500KB gate)
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

MAX_KB=500
MAX_BYTES=$((MAX_KB * 1024))
# Android store assets and design references (pre-existing product repo)
ALLOWLIST=(
  "fastlane/metadata/android/en-US/images/icon.png"
  "docs/design/icon-reference-512.png"
  "icon.png"
  "app/src/main/res/drawable/ic_launcher.png"
  "app/src/main/generated/baselineProfiles/baseline-prof.txt"
  "app/src/main/generated/baselineProfiles/startup-prof.txt"
)
ERRORS=0
MAX_REPORT=20
reported=0

large_sizes="$(git cat-file --batch-check='%(objectsize) %(rest)' < <(git ls-files | awk '{print "HEAD:" $0 " " $0}') 2>/dev/null || true)"
while IFS=' ' read -r size file; do
  [ -z "${file:-}" ] || [ -z "${size:-}" ] && continue
  case "$size" in
    ''|*[!0-9]*) continue ;;
  esac
  skip=false
  for allowed in "${ALLOWLIST[@]}"; do
    if [ "$file" = "$allowed" ]; then skip=true; break; fi
  done
  if [ "$skip" = true ]; then continue; fi
  if [ "$size" -gt "$MAX_BYTES" ]; then
    kb=$((size / 1024))
    echo "LARGE TRACKED FILE: $file (${kb} KB > ${MAX_KB} KB)"
    ERRORS=$((ERRORS + 1))
    reported=$((reported + 1))
    if [ "$reported" -ge "$MAX_REPORT" ]; then
      echo "... truncated (max $MAX_REPORT)"
      break
    fi
  fi
done <<< "$large_sizes"

if [ "$ERRORS" -gt 0 ]; then
  echo "$ERRORS tracked file(s) exceed ${MAX_KB} KB"
  exit 1
fi

echo "Large tracked file check passed"
