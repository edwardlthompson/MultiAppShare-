#!/usr/bin/env python3
"""Emit Dependabot PR rows: number|verdict|mergeable|title|url (or NONE)."""
from __future__ import annotations

import json
import sys
from pathlib import Path


def main() -> int:
    path = Path(sys.argv[1])
    prs = json.loads(path.read_text(encoding="utf-8"))
    deps = []
    for p in prs:
        login = ((p.get("author") or {}).get("login") or "").lower()
        if "dependabot" in login:
            deps.append(p)
    if not deps:
        print("NONE")
        return 0
    ok = {"SUCCESS", "SKIPPED", "NEUTRAL", None}
    for p in sorted(deps, key=lambda x: x["number"]):
        checks = p.get("statusCheckRollup") or []
        failing = [
            c
            for c in checks
            if c.get("status") == "COMPLETED" and c.get("conclusion") not in ok
        ]
        pending = [c for c in checks if c.get("status") != "COMPLETED"]
        if failing:
            verdict = "RED"
        elif pending:
            verdict = "PENDING"
        else:
            verdict = "GREEN"
        mergeable = p.get("mergeable") or "UNKNOWN"
        title = (p.get("title") or "").replace("|", "/")
        url = p.get("url") or ""
        print(f"{p['number']}|{verdict}|{mergeable}|{title}|{url}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
