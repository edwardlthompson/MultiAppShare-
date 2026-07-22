#!/usr/bin/env bash
# Android child: validate TEMPLATE_INDEX.json entry_points + files[] exist.
# Does not require every scripts/*.sh to be indexed (full-template check).
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
INDEX="$ROOT/TEMPLATE_INDEX.json"

if [ ! -f "$INDEX" ]; then
  echo "MISSING: TEMPLATE_INDEX.json"
  exit 1
fi

python3 - "$INDEX" "$ROOT" <<'PY'
import json
import os
import sys

index_path, root = sys.argv[1], sys.argv[2]
with open(index_path, encoding="utf-8") as f:
    data = json.load(f)

errors = []
for ep in data.get("entry_points", {}).values():
    if ep and not os.path.exists(os.path.join(root, ep)):
        errors.append(ep)
for item in data.get("files", []):
    path = item.get("path")
    if path and not os.path.exists(os.path.join(root, path)):
        errors.append(path)
for mod in data.get("modules", {}).values():
    guide = mod.get("guide")
    if guide and not os.path.exists(os.path.join(root, guide)):
        errors.append(guide)
    example = mod.get("example")
    if example and not os.path.exists(os.path.join(root, example)):
        errors.append(example)

if errors:
    print("Missing paths:", *errors, sep="\n  ")
    sys.exit(1)

version = data.get("template_version", "")
tv_path = os.path.join(root, ".template-version")
if os.path.isfile(tv_path):
    file_ver = open(tv_path, encoding="utf-8").read().strip()
    if version and file_ver and version != file_ver:
        print(f"FAIL: TEMPLATE_INDEX template_version ({version}) != .template-version ({file_ver})")
        sys.exit(1)

print("TEMPLATE_INDEX.json validation passed (android child)")
PY
