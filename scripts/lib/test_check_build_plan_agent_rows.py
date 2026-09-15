"""Tests for invisible BUILD_PLAN AGENT row detection."""
from __future__ import annotations

import unittest

from check_build_plan_agent_rows import check_text, markdown_open_agent_count, parser_table_open_count


VISIBLE = """
## Milestone Halt

| Status | ID | Label | Task |
|--------|----|-------|------|
| 🔲 | BH-001 | `[AGENT]` | Install hook |
| 🔲 | BH-002 | `[AGENT]` | Wire SDK |
"""

SEVEN_COL = """
## Milestone UX

| Status | ID | Sev | Effort | Impact | Label | Task |
|--------|----|-----|--------|--------|-------|------|
| 🔲 | UX-001 | Critical | S | 9 | `[AGENT]` | Hidden permission |
"""

PARALLEL_TRAP = """
## Milestone Halt

### Parallelization

| Status | ID | Label | Task |
|--------|----|-------|------|
| 🔲 | BH-001 | `[AGENT]` | Install hook |
"""


class CheckBuildPlanAgentRowsTest(unittest.TestCase):
    def test_visible_four_col_ok(self) -> None:
        self.assertEqual(markdown_open_agent_count(VISIBLE), 2)
        self.assertEqual(parser_table_open_count(VISIBLE), 2)
        self.assertEqual(check_text(VISIBLE), [])

    def test_seven_col_fails(self) -> None:
        self.assertEqual(markdown_open_agent_count(SEVEN_COL), 1)
        self.assertEqual(parser_table_open_count(SEVEN_COL), 0)
        errors = check_text(SEVEN_COL)
        self.assertTrue(errors)

    def test_parallel_heading_hides_table(self) -> None:
        self.assertEqual(markdown_open_agent_count(PARALLEL_TRAP), 1)
        self.assertEqual(parser_table_open_count(PARALLEL_TRAP), 0)
        self.assertTrue(check_text(PARALLEL_TRAP))


if __name__ == "__main__":
    unittest.main()
