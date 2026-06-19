# MultiAppShare BUILD PLAN – Living Checklist

> **Completed milestones:** A–T, **U** (bootstrap v0.11.0), **W** (post-U audit) — [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md)  
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

**Agent rule:** Sequential lane first, then Parallel with isolated scopes ([`PARALLEL_AGENT_SCOPES.md`](PARALLEL_AGENT_SCOPES.md)). After each `[AGENT]` step: `bash scripts/watch-agent-gates.sh --once --autofix`.

```bash
grep '\[AGENT\]' docs/BUILD_PLAN.md
```

Slash shortcuts: `/verify` before merge · `/prerelease` or `/ship` before each `v*` tag · [`docs/help/BATCH_COMMANDS.md`](help/BATCH_COMMANDS.md)

---

## Milestone gates (mandatory)

Before closing any milestone:

1. `./gradlew lint test detekt koverXmlReport assembleDebug` — all pass
2. `bash scripts/validate-bootstrap.sh --quick`
3. `bash scripts/check-file-limits.sh`
4. Device smoke when UX touched — [`RELEASE_SMOKE.md`](RELEASE_SMOKE.md) · evidence in [`GATES.md`](GATES.md)
5. No untriaged **P1** defects from that milestone's scope

**Product scope:** Phones and tablets only. Wear OS and Android TV are out of scope.

---

## Pre-release gate (each `v*` tag)

Per [`PRE_RELEASE_AUDIT.md`](PRE_RELEASE_AUDIT.md). Prefer `/ship` or `/prerelease` for the automated portion.

| Item | Owner | When |
|------|-------|------|
| Automated Gradle / Paparazzi / metadata | `[AGENT]` / CI | Every tag |
| Release smoke on device | `[ADB]` `[HUMAN]` | Every tag |
| TalkBack / 200% font | `[HUMAN]` | When UX changed |
| Tag + F-Droid + GitHub Release | `[HUMAN]` | Every tag |

---

## Active board

_No active milestone._ Pick one candidate below when planning.

### Milestone V — candidates (pick one)

| ID | Owner | Task |
|----|-------|------|
| V.1 | `[AGENT]` | Re-enable Gradle configuration cache (`org.gradle.configuration-cache=true`) after AGP 9 validation (**E.5**) |
| V.2 | `[HUMAN]` | Dependabot / security advisory triage (**T.0.1**) — [`SECURITY_TRIAGE.md`](SECURITY_TRIAGE.md) |
| V.3 | `[AGENT]` | `targetSdk` / platform behavior review when raising beyond **35** — [`TARGET_SDK_REVIEW.md`](TARGET_SDK_REVIEW.md) |

**Toolchain (Milestone T):** Gradle **9.5.1** · AGP **9.2.1** · Kotlin **2.4.0** · `compileSdk` **37** — [`COMPLETED_TASKS.md#milestone-t--dependabot-dependency-upgrades`](COMPLETED_TASKS.md#milestone-t--dependabot-dependency-upgrades)

---

## Ongoing maintenance

### Weekly

- `[AUTO]` `bash scripts/check-security-triage.sh --wait-ci 300`
- `[AGENT]` Triage Dependabot PRs; keep `main` CI green
- `[AGENT]` `/maintain` or `/triage` + `/verify` when deps drift

### Pre-release (every `v*` tag)

- `[AGENT]` `/ship` or `/prerelease` → `/regress`
- `[ADB]` `[HUMAN]` [`RELEASE_SMOKE.md`](RELEASE_SMOKE.md)

---

## Archived sprints

| Sprint | Status | Archive |
|--------|--------|---------|
| A–T | Complete | [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md) |
| U — Bootstrap v0.11.0 alignment | Complete (2026-06-19) | [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md) · [`BOOTSTRAP_TEMPLATE_MAP.md`](BOOTSTRAP_TEMPLATE_MAP.md) |
| W — Post-U `/audit` | Complete (2026-06-19) | [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md) · [`GATES.md`](GATES.md) |
