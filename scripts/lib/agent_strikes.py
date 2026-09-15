"""Classify feature-gate failures so environment misses do not burn 3-strike."""
from __future__ import annotations

ENV_STAGES = frozenset({"environment", "pre-commit-hooks"})
ENV_LOG_MARKERS = ("SDK location not found",)


def is_env_failure(exit_code: int, failed_stage: str = "", log_tail: str = "") -> bool:
    if int(exit_code) == 2:
        return True
    stage = (failed_stage or "").strip()
    if stage in ENV_STAGES:
        return True
    tail = log_tail or ""
    return any(marker in tail for marker in ENV_LOG_MARKERS)


def should_reset_halt_strikes(strikes: int) -> bool:
    """True when a new `/build` should clear leftover halt strikes."""
    return int(strikes) >= 3


def next_strikes(
    current: int,
    exit_code: int,
    failed_stage: str = "",
    log_tail: str = "",
    *,
    no_increment: bool = False,
) -> int:
    if int(exit_code) == 0:
        return 0
    if no_increment or is_env_failure(exit_code, failed_stage, log_tail):
        return int(current)
    return int(current) + 1
