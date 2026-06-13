# MultiAppShare BUILD PLAN – Living Checklist

Completed milestones **A–S** are archived in [`docs/COMPLETED_TASKS.md`](COMPLETED_TASKS.md).

Agent workspace: [`AGENTS.md`](../AGENTS.md), [`AGENT_MEMORY.md`](../AGENT_MEMORY.md), [`KNOWLEDGE_BASE.md`](../KNOWLEDGE_BASE.md).

---

## Roadmap process — milestone gates (mandatory)

Before closing a milestone and starting the next:

1. **Automated gate**: `./gradlew lint test detekt koverXmlReport assembleDebug` — all **pass**.
2. **File size gate**: `pwsh scripts/check-file-limits.ps1 -Fail`
3. **Manual smoke** when UX is touched: device/emulator pass; note under Evidence.
4. **Regression**: no untriaged **P1** defects from that milestone's scope.

**Product scope:** Phones and tablets only. Wear OS and Android TV are out of scope.

---

## Pre-release gate (each `v*` tag)

Required **before every release** per [`docs/PRE_RELEASE_AUDIT.md`](PRE_RELEASE_AUDIT.md). Milestone **R.5.2** and **S.9.2** first passes archived in [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md).

| Item | Owner | When |
|------|-------|------|
| **R.5.1** Automated Gradle/Paparazzi/metadata | `[Agent]` / CI | Every tag |
| **R.5.2** Release smoke on device | `[ADB]` `[Human]` | Every tag — see [`RELEASE_SMOKE.md`](RELEASE_SMOKE.md) |
| **R.5.3** TalkBack / 200% font | `[Human]` | When UX changed |
| **R.5.4** Tag + F-Droid + GitHub Release | `[Human]` | Every tag |

---

## Next milestone

No active milestone. Append the next letter (e.g. **T**) here when scoped; archive completed work to [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md).
