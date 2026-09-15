"""Tests for agent_strikes env exemption."""
from __future__ import annotations

import unittest

from agent_strikes import is_env_failure, next_strikes, should_reset_halt_strikes


class AgentStrikesTest(unittest.TestCase):
    def test_success_resets(self) -> None:
        self.assertEqual(next_strikes(3, 0, "android-lint", "unused"), 0)

    def test_environment_exit_2_does_not_increment(self) -> None:
        self.assertEqual(next_strikes(0, 2, "environment", "no sdk"), 0)
        self.assertTrue(is_env_failure(2, "environment", ""))

    def test_pre_commit_hooks_does_not_increment(self) -> None:
        self.assertEqual(next_strikes(1, 1, "pre-commit-hooks", "missing hook"), 1)

    def test_sdk_location_log_does_not_increment(self) -> None:
        tail = "SDK location not found. Define a valid SDK location"
        self.assertEqual(next_strikes(2, 1, "android-lint", tail), 2)

    def test_real_lint_increments(self) -> None:
        self.assertEqual(next_strikes(0, 1, "android-lint", "UnusedResources"), 1)

    def test_halt_reset_at_three(self) -> None:
        self.assertTrue(should_reset_halt_strikes(3))
        self.assertFalse(should_reset_halt_strikes(2))
        self.assertFalse(should_reset_halt_strikes(0))

    def test_no_increment_keeps_current_on_failure(self) -> None:
        self.assertEqual(
            next_strikes(1, 1, "android-detekt", "ImportOrdering", no_increment=True),
            1,
        )

    def test_success_resets_even_with_no_increment(self) -> None:
        self.assertEqual(
            next_strikes(2, 0, "android-detekt", "", no_increment=True),
            0,
        )


if __name__ == "__main__":
    unittest.main()
