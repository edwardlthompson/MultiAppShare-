#!/usr/bin/env bash
# Regression: 3-strike logic increments on feature-gate failures without --step.
# Usage: scripts/verify-agent-strikes.sh
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
PROGRESS="$ROOT/.cursor/agent-progress.json"
mkdir -p "$ROOT/.cursor"
rm -f "$PROGRESS"

# shellcheck source=lib/resolve-python.sh
. "$(cd "$(dirname "$0")" && pwd)/lib/resolve-python.sh"

strikes() {
  "$PY" -c "import json,sys; print(json.load(open(sys.argv[1], encoding='utf-8')).get('strikes',0))" "$PROGRESS" 2>/dev/null || echo 0
}

for _ in 1 2 3; do
  bash scripts/agent-progress.sh record --gate feature-gate --exit 1 >/dev/null
done

s="$(strikes)"
if [ "$s" -lt 3 ]; then
  echo "FAIL: expected strikes>=3 after 3 gate failures, got $s"
  exit 1
fi

bash scripts/agent-progress.sh record --gate feature-autofix --exit 0 --autofix >/dev/null
s="$(strikes)"
if [ "$s" -lt 3 ]; then
  echo "FAIL: autofix reset strikes (got $s)"
  exit 1
fi

bash scripts/agent-progress.sh record --gate feature-gate --exit 0 >/dev/null
s="$(strikes)"
if [ "$s" != "0" ]; then
  echo "FAIL: gate success should reset strikes (got $s)"
  exit 1
fi

bash scripts/agent-progress.sh record --gate feature-gate --exit 2 --failed-stage environment --log-tail "no sdk" >/dev/null
s="$(strikes)"
if [ "$s" != "0" ]; then
  echo "FAIL: environment exit 2 incremented strikes (got $s)"
  exit 1
fi

bash scripts/agent-progress.sh record --gate feature-gate --exit 1 --failed-stage pre-commit-hooks --log-tail "missing hook" >/dev/null
s="$(strikes)"
if [ "$s" != "0" ]; then
  echo "FAIL: pre-commit-hooks incremented strikes (got $s)"
  exit 1
fi

bash scripts/agent-progress.sh record --gate feature-gate --exit 1 --failed-stage android-lint --log-tail "SDK location not found" >/dev/null
s="$(strikes)"
if [ "$s" != "0" ]; then
  echo "FAIL: SDK location log incremented strikes (got $s)"
  exit 1
fi

bash scripts/agent-progress.sh record --gate feature-gate --exit 1 --failed-stage android-lint --log-tail "UnusedResources" >/dev/null
s="$(strikes)"
if [ "$s" != "1" ]; then
  echo "FAIL: real android-lint should increment (got $s)"
  exit 1
fi

bash scripts/agent-progress.sh record --gate feature-gate --exit 1 --failed-stage android-detekt --log-tail "ImportOrdering" --no-increment >/dev/null
s="$(strikes)"
if [ "$s" != "1" ]; then
  echo "FAIL: --no-increment should keep strikes (got $s)"
  exit 1
fi

bash scripts/agent-progress.sh record --gate feature-gate --exit 1 --failed-stage android-detekt --log-tail "ImportOrdering" >/dev/null
bash scripts/agent-progress.sh record --gate feature-gate --exit 1 --failed-stage android-detekt --log-tail "ImportOrdering" >/dev/null
s="$(strikes)"
if [ "$s" != "3" ]; then
  echo "FAIL: expected strikes=3 after leftover code halt (got $s)"
  exit 1
fi

"$PY" - "$PROGRESS" << 'PY'
from pathlib import Path
import json
p = Path(".cursor/last-feature-gate.json")
p.write_text(
    json.dumps({"failed_stage": "android-detekt", "exit_code": 1, "log_tail": "ImportOrdering"})
    + "\n",
    encoding="utf-8",
)
PY

bash scripts/agent-progress.sh maybe-reset-env-strikes >/dev/null
s="$(strikes)"
if [ "$s" != "3" ]; then
  echo "FAIL: maybe-reset-env-strikes cleared a code halt (got $s)"
  exit 1
fi

bash scripts/agent-progress.sh maybe-reset-halt-strikes >/dev/null
s="$(strikes)"
if [ "$s" != "0" ]; then
  echo "FAIL: maybe-reset-halt-strikes should clear leftover 3 (got $s)"
  exit 1
fi

echo "verify-agent-strikes passed"
