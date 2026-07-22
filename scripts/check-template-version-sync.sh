#!/usr/bin/env bash
# Android child: .template-version must match TEMPLATE_INDEX.json (no release-please)
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

if [ ! -f .template-version ] || [ ! -f TEMPLATE_INDEX.json ]; then
  echo "MISSING: .template-version or TEMPLATE_INDEX.json"
  exit 1
fi

VERSION="$(tr -d '[:space:]' < .template-version)"
IDX="$(python3 -c "import json; print(json.load(open('TEMPLATE_INDEX.json', encoding='utf-8'))['template_version'])")"
if [ "$IDX" != "$VERSION" ]; then
  echo "FAIL: TEMPLATE_INDEX template_version ($IDX) != .template-version ($VERSION)"
  echo "Fix: bash scripts/sync-template-version.sh"
  exit 1
fi

echo "Template version sync OK ($VERSION)"
