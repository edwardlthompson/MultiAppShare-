#!/usr/bin/env bash
# Enable optional Security Scan (Trivy) + OpenSSF Scorecard workflows.
# Usage:
#   bash scripts/enable-optional-security-workflows.sh          # status
#   bash scripts/enable-optional-security-workflows.sh --apply  # write workflows if missing
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

APPLY=false
for arg in "$@"; do
  case "$arg" in
    --apply) APPLY=true ;;
    -h|--help)
      echo "Usage: $0 [--apply]"
      exit 0
      ;;
  esac
done

SEC=".github/workflows/security.yml"
SCORE=".github/workflows/scorecard.yml"

status_one() {
  local f="$1"
  if [ -f "$f" ]; then
    echo "PRESENT  $f"
  else
    echo "MISSING  $f"
  fi
}

echo "=== Optional security workflows ==="
status_one "$SEC"
status_one "$SCORE"

if [ "$APPLY" != true ]; then
  echo "Pass --apply to create any MISSING workflow files."
  exit 0
fi

python3 "$ROOT/scripts/lib/write_optional_security_workflows.py"
echo "Done. check-github-ci.sh / check-security-triage.sh will pick these up on next run."
