#!/usr/bin/env bash
# Android child: sync TEMPLATE_INDEX (+ optional README badge) from .template-version
# (Does not require .release-please-manifest.json — MultiAppShare does not use release-please.)
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

if [ ! -f .template-version ]; then
  echo "MISSING: .template-version"
  exit 1
fi

VERSION="$(tr -d '[:space:]' < .template-version)"
if [ -z "$VERSION" ]; then
  echo "FAIL: empty .template-version"
  exit 1
fi

export SYNC_TEMPLATE_VERSION="${VERSION}"
python3 <<'PY'
import json
import os
import re
from pathlib import Path

version = os.environ["SYNC_TEMPLATE_VERSION"]
idx = Path("TEMPLATE_INDEX.json")
if idx.is_file():
    data = json.loads(idx.read_text(encoding="utf-8"))
    data["template_version"] = version
    idx.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")

readme = Path("README.md")
if readme.is_file():
    text = readme.read_text(encoding="utf-8")
    text2 = re.sub(
        r"!\[Template\]\(https://img\.shields\.io/badge/template-[\d.]+",
        f"![Template](https://img.shields.io/badge/template-{version}",
        text,
    )
    text2 = re.sub(
        r"Current template version: \*\*[\d.]+\*\*",
        f"Current template version: **{version}**",
        text2,
    )
    if text2 != text:
        readme.write_text(text2, encoding="utf-8")
PY

echo "Synced template version to ${VERSION} (TEMPLATE_INDEX.json from .template-version)"
