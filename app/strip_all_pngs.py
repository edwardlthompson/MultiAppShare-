#!/usr/bin/env python3
"""Strip non-essential PNG chunks (EXIF, pHYs, tIME) from F-Droid / Fastlane images."""
from __future__ import annotations

import glob
import os
import struct
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

KEEP_TYPES = {b"IHDR", b"IDAT", b"IEND", b"PLTE", b"tRNS"}


def strip_png(path: str) -> None:
    print(f"Binary stripping PNG: {path}")
    if not os.path.exists(path):
        print("File not found.")
        return

    with open(path, "rb") as f:
        header = f.read(8)
        if header != b"\x89PNG\r\n\x1a\n":
            print(f"Not a PNG file (Header: {header}). Skipping.")
            return

        chunks = []
        while True:
            chunk_header = f.read(8)
            if not chunk_header:
                break
            length, chunk_type = struct.unpack(">I4s", chunk_header)
            data = f.read(length)
            crc = f.read(4)

            if chunk_type in KEEP_TYPES:
                chunks.append((length, chunk_type, data, crc))
            else:
                print(f"  Stripping chunk: {chunk_type.decode('ascii')} ({length} bytes)")

    with open(path, "wb") as f:
        f.write(b"\x89PNG\r\n\x1a\n")
        for length, chunk_type, data, crc in chunks:
            f.write(struct.pack(">I4s", length, chunk_type))
            f.write(data)
            f.write(crc)
    print("  Strip complete.")


def main() -> int:
    os.chdir(ROOT)
    patterns = [
        "metadata/*.png",
        "fastlane/metadata/android/**/*.png",
        "app/src/main/res/**/*.png",
    ]
    for pattern in patterns:
        for path in glob.glob(pattern, recursive=True):
            strip_png(path)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
