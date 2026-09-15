#!/usr/bin/env bash
# Fail when BUILD_PLAN 🔲 [AGENT] rows are invisible to build-sprint-status.
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
# shellcheck source=lib/resolve-python.sh
. "$(cd "$(dirname "$0")" && pwd)/lib/resolve-python.sh"
exec "$PY" "$ROOT/scripts/lib/check_build_plan_agent_rows.py" "$ROOT"
