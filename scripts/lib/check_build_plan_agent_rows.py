"""Fail when 🔲 [AGENT]/[AUTO] BUILD_PLAN rows are invisible to /build."""
from __future__ import annotations

import re
import sys
from pathlib import Path

from build_sprint_model import TABLE_OPEN
from build_sprint_parse import parse_sprint_blocks, split_sprint_phases
from build_sprint_resolve import resolve_sprint

MARKDOWN_OPEN_AGENT = re.compile(
    r"^\|\s*(?:🔲|⬜|\[ \])\s*\|.*`\[(?P<owner>AGENT|AUTO)\]`"
)
NUMBERED_OPEN_AGENT = re.compile(
    r"^(?:\d+[a-z]?\.)\s+(?:🔲|⬜|\[ \])\s+\[(?P<owner>AGENT|AUTO)\]\s+"
)
BULLET_OPEN_AGENT = re.compile(
    r"^- (?:🔲|⬜|\[ \]) \[(?P<owner>AGENT|AUTO)\]\s+"
)


def markdown_open_agent_count(text: str) -> int:
    count = 0
    for line in text.splitlines():
        if MARKDOWN_OPEN_AGENT.match(line) or NUMBERED_OPEN_AGENT.match(line) or BULLET_OPEN_AGENT.match(line):
            count += 1
    return count


def parser_open_agent_count(text: str) -> int:
    total = 0
    for _title, block_lines in parse_sprint_blocks(text):
        status = resolve_sprint(_title, block_lines, progress={}, backlog_keys=set())
        total += int(status.get("open_agent_auto") or 0)
    return total


def parser_table_open_count(text: str) -> int:
    count = 0
    for _title, block_lines in parse_sprint_blocks(text):
        pre, _parallel, post, _human = split_sprint_phases(block_lines)
        for line in pre + post:
            match = TABLE_OPEN.match(line)
            if match and match.group("owner") in ("AGENT", "AUTO"):
                count += 1
    return count


def check_text(text: str) -> list[str]:
    errors: list[str] = []
    md = markdown_open_agent_count(text)
    parsed = parser_open_agent_count(text)
    table = parser_table_open_count(text)
    if md > parsed:
        errors.append(
            f"BUILD_PLAN has {md} open [AGENT]/[AUTO] rows but build-sprint-status sees {parsed}. "
            "Use ## Milestone, a 4-column Status|ID|`[AGENT]`|Task table, and put the table "
            "before any heading matching Parallel."
        )
    if md > table and table < parsed:
        errors.append(
            f"TABLE_OPEN matched {table} rows; markdown counted {md}. Extra columns hide AGENT work from /build."
        )
    return errors


def main(argv: list[str] | None = None) -> int:
    args = argv if argv is not None else sys.argv[1:]
    root = Path(args[0]).resolve() if args else Path(__file__).resolve().parents[2]
    plan = root / "docs" / "BUILD_PLAN.md"
    if not plan.is_file():
        plan = root / "BUILD_PLAN.md"
    if not plan.is_file():
        print("FAIL: BUILD_PLAN.md not found", file=sys.stderr)
        return 1
    errors = check_text(plan.read_text(encoding="utf-8"))
    if errors:
        for err in errors:
            print(f"FAIL: {err}", file=sys.stderr)
        return 1
    print("OK   BUILD_PLAN AGENT rows are visible to build-sprint-status")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
