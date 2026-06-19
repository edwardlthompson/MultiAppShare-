#!/usr/bin/env bash
# Pre-release gate — Multi App Share
# Usage: scripts/pre-release-gate.sh
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

ERRORS=0
VERSION=""

echo "=== Pre-release gate (Multi App Share) ==="

STACK="${STACK:-android}"
if [ -f .cursor/stack-selection.json ]; then
  STACK="$(python3 -c "import json; print(json.load(open('.cursor/stack-selection.json')).get('stack','android'))" 2>/dev/null || echo android)"
fi

if ! bash scripts/feature-gate.sh --stack "$STACK" --strict --json; then
  echo "FAIL: feature-gate.sh"
  ERRORS=$((ERRORS + 1))
else
  echo "OK   feature-gate.sh passed"
fi

if ! bash scripts/check-security-triage.sh --wait-ci 0; then
  echo "WARN: security-triage.sh (non-strict; review Dependabot manually)"
fi

if [ ! -f .template-version ]; then
  echo "MISSING: .template-version"
  ERRORS=$((ERRORS + 1))
else
  VERSION="$(tr -d '[:space:]' < .template-version)"
  echo "OK   .template-version = ${VERSION}"
fi

if [ -f app/build.gradle.kts ]; then
  APP_VERSION="$(grep 'versionName' app/build.gradle.kts | head -1 | sed 's/.*"\(.*\)".*/\1/')"
  echo "OK   app versionName = ${APP_VERSION}"
fi

echo ""
echo "REMINDER: Complete docs/PRE_RELEASE_AUDIT.md and docs/RELEASE_SMOKE.md before tagging."
echo "F-Droid: FDROID_MAINTENANCE.md"

if [ "$ERRORS" -gt 0 ]; then
  echo "${ERRORS} pre-release gate check(s) failed"
  exit 1
fi

echo "Pre-release gate passed"
