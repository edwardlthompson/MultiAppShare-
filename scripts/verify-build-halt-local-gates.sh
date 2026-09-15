#!/usr/bin/env bash
# Unit + hook regressions for BH-001–BH-007 (build-halt-local-gates).
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
# shellcheck source=lib/resolve-python.sh
. "$(cd "$(dirname "$0")" && pwd)/lib/resolve-python.sh"

"$PY" -m unittest discover -s "$ROOT/scripts/lib" -p 'test_agent_strikes.py' -v
"$PY" -m unittest discover -s "$ROOT/scripts/lib" -p 'test_android_sdk.py' -v
"$PY" -m unittest discover -s "$ROOT/scripts/lib" -p 'test_check_build_plan_agent_rows.py' -v
bash "$ROOT/scripts/verify-agent-strikes.sh"
bash "$ROOT/scripts/check-build-plan-agent-rows.sh"
bash "$ROOT/scripts/verify-commit-msg-hook-install.sh"

if grep -q 'trailing-whitespace end-of-file-fixer' "$ROOT/scripts/feature-autofix.sh"; then
  echo "FAIL: feature-autofix.sh must run one pre-commit hook ID at a time"
  exit 1
fi
if ! grep -q 'pre-commit run trailing-whitespace --files' "$ROOT/scripts/feature-autofix.sh"; then
  echo "FAIL: missing trailing-whitespace autofix invocation"
  exit 1
fi
if ! grep -q 'pre-commit run end-of-file-fixer --files' "$ROOT/scripts/feature-autofix.sh"; then
  echo "FAIL: missing end-of-file-fixer autofix invocation"
  exit 1
fi
if ! grep -q 'maybe-reset-halt-strikes' "$ROOT/.cursor/commands/build.md"; then
  echo "FAIL: /build Step 0 must call maybe-reset-halt-strikes"
  exit 1
fi
if grep -q 'maybe-reset-halt-strikes' "$ROOT/scripts/watch-agent-gates.sh"; then
  echo "FAIL: watch-agent-gates must not reset halt strikes mid-run"
  exit 1
fi
if ! grep -q 'FEATURE_GATE_SKIP_STRIKE' "$ROOT/scripts/watch-agent-gates.sh"; then
  echo "FAIL: watch-agent-gates autofix retry must skip strike increment"
  exit 1
fi
echo "verify-build-halt-local-gates passed"
