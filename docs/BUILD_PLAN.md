# MultiAppShare BUILD PLAN - Living Checklist

> **Completed milestones:** A-T, **U**, **W**, **V**, **X** - [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md)
> **Active:** Milestone **AA** - Group and share UX (rename, merge, theme, delay)
> **Agent workspace:** [`AGENTS.md`](../AGENTS.md) / [`AGENT_MEMORY.md`](../AGENT_MEMORY.md) / [`START_HERE.md`](START_HERE.md)
> **Human backlog:** [`HUMAN_BACKLOG.md`](../HUMAN_BACKLOG.md)
> **Gate log:** [`GATES.md`](GATES.md)

---

## Owner labels

| Label | Owner | When to use |
|-------|-------|-------------|
| `[AGENT]` | Cursor Agent | Code, docs, tests, CI config |
| `[HUMAN]` | Human developer | Approvals, GitHub settings, product decisions |
| `[ADB]` | Human (Android) | Device/emulator testing, F-Droid |
| `[AUTO]` | CI / scripts | GitHub Actions, Dependabot, gate scripts |
**Status markers (active board only):** 🔲 open / ✅ done / ❌ blocked - emoji only (never GitHub checkboxes).

**Agent rule:** Sequential lane first; after each `[AGENT]` step: `bash scripts/watch-agent-gates.sh --once --autofix`

---

## Milestone gates (mandatory)

1. `./gradlew lint test detekt koverXmlReport assembleDebug`
2. `bash scripts/validate-bootstrap.sh --quick`
3. `bash scripts/check-file-limits.sh`
4. Device smoke when UX touched - [`RELEASE_SMOKE.md`](RELEASE_SMOKE.md)
5. No untriaged **P1** defects

**Product scope:** Phones and tablets only.

---

## Pre-release gate (each `v*` tag)

Per [`PRE_RELEASE_AUDIT.md`](PRE_RELEASE_AUDIT.md). Prefer `/ship`.

---

## Milestone Y - Bootstrap 0.15.0 alignment

### Sequential

| Status | ID | Label | Task |
|--------|----|-------|------|
| ✅ | Y.1 | `[AGENT]` | Write `docs/BOOTSTRAP_ALIGNMENT.md`; reshape this board; seed `HUMAN_BACKLOG.md` |
| ✅ | Y.2 | `[AGENT]` | Port Cursor surface (local-compute, cleanup, skills, agents, FOSS hooks, TEMPLATE_INDEX, upgrade docs); bump `.template-version` |
| ✅ | Y.3 | `[AGENT]` | Port template-update/sync + parallel gate helpers; green `validate-bootstrap --quick` |
| ✅ | Y.4 | `[AGENT]` | Add `dependency-review.yml` (+ optional stale/weekly-health); fix `ci.yml` doc drift |
| ✅ | Y.5 | `[AGENT]` | Refresh `modules/android/MODULE.md` + README agent pointer |
| ✅ | Y.6 | `[AGENT]` | Run gates; close memory / DECISION_LOG / migration notes |
### Parallel (after Y.2 Sequential lock)

| Status | ID | Label | Task |
|--------|----|-------|------|
| ✅ | Y.P1 | `[AGENT]` | Adapt skill/agent path refs to `docs/BUILD_PLAN.md` (non-overlapping with Y.3 scripts) |
| ✅ | Y.P2 | `[AGENT]` | Doc drift: FEATURE_MODULES / PARALLEL_AGENT_SCOPES to `android.yml` naming |
### Human & device (after automation)

| Status | ID | Label | Task |
|--------|----|-------|------|
| 🔲 | Y.H1 | `[HUMAN]` | Confirm branch protection still requires **Android CI** + **CodeQL** (no silent rename) |
| 🔲 | Y.H2 | `[HUMAN]` | Review FOSS Cursor hooks (encoding + shell denylist) after first Agent session |
| 🔲 | Y.H3 | `[HUMAN]` | Triage Dependabot PRs (#27-#32 / successors) via `triage-dependabot-prs.sh --apply` |
| 🔲 | Y.H4 | `[HUMAN]` | Push any unpushed security workflows if still local-only |
| ✅ | Y.A1 | `[ADB]` | Covered by Z.A1 / AA.A1 (UX was touched) |
---

## Milestone Z - Share session UX

### Sequential

| Status | ID | Label | Task |
|--------|----|-------|------|
| ✅ | Z.1 | `[AGENT]` | Resume in-flight share after process death (DataStore snapshot + nonce) |
| ✅ | Z.2 | `[AGENT]` | Re-share last payload from History |
| ✅ | Z.3 | `[AGENT]` | Finish early / skip remaining mid-sequence |
| ✅ | Z.4 | `[AGENT]` | Duplicate group |
| ✅ | Z.5 | `[AGENT]` | Tablet two-pane list + editor |
| ✅ | Z.6 | `[AGENT]` | Predictive Back on share overlay |
| ✅ | Z.7 | `[AGENT]` | In-app language picker (en/fr/es/system) |
### Human & device

| Status | ID | Label | Task |
|--------|----|-------|------|
| ✅ | Z.A1 | `[ADB]` | CPH2583 API 36 · debug 1.9.3 (179): process-death resume, language dialog, skip remaining; `ShareUxSmokeInstrumentedTest` tablet + skip |
---

## Milestone AA - Group and share UX

### Sequential

| Status | ID | Label | Task |
|--------|----|-------|------|
| ✅ | AA.1 | `[AGENT]` | Persist inbound content URI grants |
| ✅ | AA.2 | `[AGENT]` | Rename group (name-PK rewrite) |
| ✅ | AA.3 | `[AGENT]` | Theme system / light / dark |
| ✅ | AA.4 | `[AGENT]` | Notification Next / Skip / Cancel |
| ✅ | AA.5 | `[AGENT]` | Undo delete group |
| ✅ | AA.6 | `[AGENT]` | Skip this app only |
| ✅ | AA.7 | `[AGENT]` | Honor sharingDelay between steps |
| ✅ | AA.8 | `[AGENT]` | Merge two groups |
### Human & device

| Status | ID | Label | Task |
|--------|----|-------|------|
| ✅ | AA.A1 | `[ADB]` | CPH2583 API 36 · persistable URI resume, theme dialog/dark, notification skip extra, undo + merge (`ShareUxSmokeInstrumentedTest`) |
---

## Milestone AB — seven product slices

Spec: [`docs/features/milestone-ab.md`](features/milestone-ab.md). Room **v1→v2** Sequential lock first.

### Sequential

| Status | ID | Label | Task |
|--------|----|-------|------|
| ✅ | AB.1 | `[AGENT]` | Room v2: stable group `id` + history `payloadJson`; Migration 1→2 |
| ✅ | AB.2 | `[AGENT]` | Per-row History re-share; persist payload on share start |
| ✅ | AB.3 | `[AGENT]` | Shortcut/deeplink by `id`; heal legacy name-id pins |
| ✅ | AB.4 | `[AGENT]` | BackupWrapper v2 settings + lastPayload; import v1 compat |
| ✅ | AB.5 | `[AGENT]` | Always-on group filter when groups non-empty |
| ✅ | AB.6 | `[AGENT]` | Do not auto-advance on share fail; Retry/Replay stays |
| ✅ | AB.7 | `[AGENT]` | Overflow share-from-clipboard (text or content URI) |
| ✅ | AB.8 | `[AGENT]` | Semantics/48dp/Paparazzi + checklist (TalkBack hardware later) |

### Human & device

| Status | ID | Label | Task |
|--------|----|-------|------|
| ✅ | AB.A1 | `[ADB]` | CPH2583 API 36 · filter + clipboard + history re-share + fail-then-retry; backup v2 settings + rename keeps id (`MilestoneAbSmokeInstrumentedTest`) |

---

## Ongoing maintenance

- Weekly: `check-security-triage.sh` / `triage-dependabot-prs.sh` / `/verify`
- Pre-release: `/ship` / device smoke
- Template drift: `bash scripts/check-template-updates.sh` (after Y.3)

**Toolchain:** Gradle **9.5.1** / AGP **9.2.1** / Kotlin **2.4.0** / `compileSdk` **37** / `targetSdk` **36**

---

## Archived sprints

| Sprint | Status | Archive |
|--------|--------|---------|
| A-T | Complete | [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md) |
| U, W | Complete (2026-06-19) | [`BOOTSTRAP_TEMPLATE_MAP.md`](BOOTSTRAP_TEMPLATE_MAP.md) / [`GATES.md`](GATES.md) |
| V (V.1-V.3b, V.2) | Complete (2026-06-20) | [`TARGET_SDK_REVIEW.md`](TARGET_SDK_REVIEW.md) / [`SECURITY_TRIAGE.md`](SECURITY_TRIAGE.md) |
| X (audit + automation) | Complete (2026-07-12) | [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md) / automation scripts |
