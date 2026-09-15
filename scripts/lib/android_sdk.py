"""Detect a local Android SDK and write gitignored local.properties."""
from __future__ import annotations

import argparse
import os
import sys
from pathlib import Path


def candidate_sdk_dirs() -> list[Path]:
    out: list[Path] = []
    for key in ("ANDROID_HOME", "ANDROID_SDK_ROOT"):
        val = (os.environ.get(key) or "").strip()
        if val:
            out.append(Path(val).expanduser())
    home = Path.home()
    out.append(home / "Android" / "Sdk")
    local_app = (os.environ.get("LOCALAPPDATA") or "").strip()
    if local_app:
        out.append(Path(local_app) / "Android" / "Sdk")
    else:
        out.append(home / "AppData" / "Local" / "Android" / "Sdk")
    out.append(Path("/opt/android-sdk"))
    seen: set[str] = set()
    uniq: list[Path] = []
    for path in out:
        key = str(path)
        if key in seen:
            continue
        seen.add(key)
        uniq.append(path)
    return uniq


def first_existing_sdk(candidates: list[Path] | None = None) -> Path | None:
    for path in candidates if candidates is not None else candidate_sdk_dirs():
        if path.is_dir():
            return path.resolve()
    return None


def sdk_dir_from_local_properties(root: Path) -> Path | None:
    props = root / "local.properties"
    if not props.is_file():
        return None
    for line in props.read_text(encoding="utf-8").splitlines():
        if not line.startswith("sdk.dir="):
            continue
        raw = line.split("=", 1)[1].strip().replace("\\", "/")
        path = Path(raw)
        return path.resolve() if path.is_dir() else None
    return None


def write_local_properties(root: Path, sdk: Path) -> Path:
    props = root / "local.properties"
    sdk_str = sdk.resolve().as_posix()
    lines: list[str] = []
    if props.is_file():
        lines = [ln for ln in props.read_text(encoding="utf-8").splitlines() if not ln.startswith("sdk.dir=")]
    lines.append(f"sdk.dir={sdk_str}")
    props.write_text("\n".join(lines) + "\n", encoding="utf-8")
    return props


def ensure_android_sdk(root: Path) -> Path | None:
    found = sdk_dir_from_local_properties(root) or first_existing_sdk()
    if found is None:
        return None
    write_local_properties(root, found)
    return found


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--root", default=".", help="Repo root (local.properties location)")
    parser.add_argument("--ensure", action="store_true", help="Write local.properties when an SDK dir exists")
    parser.add_argument("--detect", action="store_true", help="Print the first existing SDK path")
    args = parser.parse_args(argv if argv is not None else sys.argv[1:])
    root = Path(args.root).resolve()
    if args.ensure:
        found = ensure_android_sdk(root)
        if found is None:
            print("Android SDK not found", file=sys.stderr)
            return 1
        print(found.as_posix())
        return 0
    if args.detect:
        found = first_existing_sdk()
        if found is None:
            return 1
        print(found.as_posix())
        return 0
    parser.print_help()
    return 2


if __name__ == "__main__":
    raise SystemExit(main())
