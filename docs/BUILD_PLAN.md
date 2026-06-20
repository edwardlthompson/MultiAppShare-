# MultiAppShare BUILD PLAN – Living Checklist

> **Completed milestones:** A–T, **U**, **W**, **V** — [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md)  
> **Agent workspace:** [`AGENTS.md`](../AGENTS.md) · [`AGENT_MEMORY.md`](../AGENT_MEMORY.md) · [`docs/START_HERE.md`](START_HERE.md)  
> **Gate log:** [`docs/GATES.md`](GATES.md)

---

## Owner labels

| Label | Owner | When to use |
|-------|-------|-------------|
| `[AGENT]` | Cursor Agent | Code, docs, tests, CI config |
| `[HUMAN]` | Human developer | Approvals, GitHub settings, product decisions |
| `[ADB]` | Human (Android) | Device/emulator testing, F-Droid |
| `[AUTO]` | CI / scripts | GitHub Actions, Dependabot, gate scripts |

**Agent rule:** Sequential lane first · after each `[AGENT]` step: `bash scripts/watch-agent-gates.sh --once --autofix`

---

## Milestone gates (mandatory)

1. `./gradlew lint test detekt koverXmlReport assembleDebug`
2. `bash scripts/validate-bootstrap.sh --quick`
3. `bash scripts/check-file-limits.sh`
4. Device smoke when UX touched — [`RELEASE_SMOKE.md`](RELEASE_SMOKE.md)
5. No untriaged **P1** defects

**Product scope:** Phones and tablets only.

---

## Pre-release gate (each `v*` tag)

Per [`PRE_RELEASE_AUDIT.md`](PRE_RELEASE_AUDIT.md). Prefer `/ship`.

---

## Active board

_No active milestone._

**Toolchain:** Gradle **9.5.1** · AGP **9.2.1** · Kotlin **2.4.0** · `compileSdk` **37** · `targetSdk` **36**

---

## Ongoing maintenance

- Weekly: `check-security-triage.sh` · Dependabot triage · `/verify`
- Pre-release: `/ship` · device smoke

---

## Archived sprints

| Sprint | Status | Archive |
|--------|--------|---------|
| A–T | Complete | [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md) |
| U, W | Complete (2026-06-19) | [`BOOTSTRAP_TEMPLATE_MAP.md`](BOOTSTRAP_TEMPLATE_MAP.md) · [`GATES.md`](GATES.md) |
| V (V.1–V.3b, V.2) | Complete (2026-06-20) | [`TARGET_SDK_REVIEW.md`](TARGET_SDK_REVIEW.md) · [`SECURITY_TRIAGE.md`](SECURITY_TRIAGE.md) |
