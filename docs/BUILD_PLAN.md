# MultiAppShare BUILD PLAN - Living Checklist

> **Completed milestones:** A-T, **U**, **W**, **V**, **X**, **Y** (agent/auto), **Z**, **AA**, **AB**, **AD** - [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md)
> **Active:** **AE** (allideas product backlog) · **AC** (ADB leftover) · Human leftovers (Y.H1–Y.H4)
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

> **Y** archived in docs/COMPLETED_TASKS.md @ `0b1880e`.
> **Z** archived in docs/COMPLETED_TASKS.md @ `0b1880e`.
> **AA** archived in docs/COMPLETED_TASKS.md @ `0b1880e`.
> **AB** archived in docs/COMPLETED_TASKS.md @ `0b1880e`.
> **AD** archived in docs/COMPLETED_TASKS.md @ `080e11a`.

## Milestone AC — Donations and product updates

Reuse Continuum Calendar’s quiet donate + daily GitHub APK check. Spec: [`docs/features/donations-and-updates.md`](features/donations-and-updates.md).

| Status | ID | Label | Task |
|--------|----|-------|------|
| ✅ | AC.1 | `[AGENT]` | Pure logic + tests: daily interval, APK filename version, newer-than-current, dismiss, donate nudge only on version change |
| ✅ | AC.2 | `[AGENT]` | Device-local prefs, GitHub `releases/latest` fetch (UA + 10s timeout), About/Menu Venmo, post-update donate note, Install/Later prompt |
| 🔲 | AC.3 | `[ADB]` | Release-APK smoke: first-run silent, menu donate, update dialog has no donate button (instrumented suite passed on debug) |
---

## Milestone Y leftovers (Archived)

All Human leftovers (Y.H1–Y.H4) verified via automation and archived in [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md).
---

## Milestone AE — Product backlog (allideas 2026-08-30) (Archived)

All 67 `[AGENT]` rows complete and archived in [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md).

### Human & device (after automation)

| Status | ID | Label | Task |
|--------|----|-------|------|
| 🔲 | AE.48 | `[ADB]` | Hardware TalkBack pass on Golden Path + share overlay (`ACCESSIBILITY_CHECKLIST`) |
| 🔲 | AE.59 | `[HUMAN]` | F-Droid `fdroiddata` MR for 1.9.5 / next tag (`FDROID_MAINTENANCE.md`) |
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
| Y (agent/auto) | Complete (2026-08-20) | [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md) |
| Z | Complete (2026-08-20) | [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md) |
| AA | Complete (2026-08-20) | [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md) |
| AB | Complete (2026-08-20) | [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md) |
| AD | Complete (2026-08-30) | [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md) |
