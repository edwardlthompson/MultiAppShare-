# MultiAppShare BUILD PLAN – Living Checklist

Completed milestones **A–T** are archived in [`docs/COMPLETED_TASKS.md`](COMPLETED_TASKS.md).

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

## Next milestone (TBD)

No active milestone. Candidate follow-ups (pick one when planning):

- **U** — Re-enable Gradle configuration cache (`org.gradle.configuration-cache=true`) after AGP 9 validation (**E.5**).
- **U** — Dependabot alerts + security advisory triage (**T.0.1**, human-only).
- **U** — `targetSdk` / platform behavior review when raising beyond **35** — see [`TARGET_SDK_REVIEW.md`](TARGET_SDK_REVIEW.md).

Toolchain after **Milestone T** (2026-06-13): Gradle **9.5.1**, AGP **9.2.1**, Kotlin **2.4.0**, `compileSdk` **37** — details in [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md#milestone-t--dependabot-dependency-upgrades).
