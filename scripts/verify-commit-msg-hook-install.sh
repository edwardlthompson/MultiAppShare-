#!/usr/bin/env bash
# Regression: check-pre-commit-hooks.sh installs a missing commit-msg hook.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

if [ "${CI:-}" = "true" ] || [ "${GITHUB_ACTIONS:-}" = "true" ]; then
  echo "OK   verify-commit-msg-hook-install skipped in CI"
  exit 0
fi

if [ ! -d .git ]; then
  echo "OK   not a git checkout"
  exit 0
fi

HOOK=".git/hooks/commit-msg"
if [ ! -f "$HOOK" ] || [ ! -s "$HOOK" ]; then
  bash scripts/check-pre-commit-hooks.sh
  if [ ! -s "$HOOK" ]; then
    echo "FAIL: check-pre-commit-hooks.sh did not install commit-msg"
    exit 1
  fi
  echo "verify-commit-msg-hook-install passed (installed missing hook)"
  exit 0
fi

backup="$(mktemp)"
cp "$HOOK" "$backup"
rm -f "$HOOK"
restore() {
  if [ ! -s "$HOOK" ] && [ -s "$backup" ]; then
    cp "$backup" "$HOOK"
    chmod +x "$HOOK" || true
  fi
  rm -f "$backup"
}
trap restore EXIT

set +e
bash scripts/check-pre-commit-hooks.sh
rc=$?
set -e

if [ "$rc" -ne 0 ]; then
  echo "FAIL: check-pre-commit-hooks.sh should auto-install after a missing hook (exit $rc)"
  exit 1
fi
if [ ! -s "$HOOK" ]; then
  echo "FAIL: commit-msg hook still missing after check"
  exit 1
fi

echo "verify-commit-msg-hook-install passed"
