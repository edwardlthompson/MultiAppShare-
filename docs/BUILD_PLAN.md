# MultiAppShare BUILD PLAN - Living Checklist

> **Completed milestones:** A-T, **U**, **W**, **V**, **X**, **Y** (agent/auto), **Z**, **AA**, **AB** - [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md)
> **Active:** **AC** (ADB leftover) · **AD** (Golden Path 1–7) · Human leftovers (Y.H1–Y.H4)
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

## Milestone AC — Donations and product updates

Reuse Continuum Calendar’s quiet donate + daily GitHub APK check. Spec: [`docs/features/donations-and-updates.md`](features/donations-and-updates.md).

| Status | ID | Label | Task |
|--------|----|-------|------|
| ✅ | AC.1 | `[AGENT]` | Pure logic + tests: daily interval, APK filename version, newer-than-current, dismiss, donate nudge only on version change |
| ✅ | AC.2 | `[AGENT]` | Device-local prefs, GitHub `releases/latest` fetch (UA + 10s timeout), About/Menu Venmo, post-update donate note, Install/Later prompt |
| 🔲 | AC.3 | `[ADB]` | Release-APK smoke: first-run silent, menu donate, update dialog has no donate button (instrumented suite passed on debug) |
---

## Milestone AD — Golden Path slices (template v1.0.0)

Port stub patterns into existing feature folders. **Never** copy `examples/android/` over the product. Sequential only (shared `MainScreen` / composition root). **One `/feature` task per row.**

| Status | ID | Label | Task |
|--------|----|-------|------|
| 🔲 | AD.1 | `[AGENT]` | `/feature` **about** — GP About surface on existing dialog/donate. Spec: [`docs/features/about.md`](features/about.md) |
| 🔲 | AD.2 | `[AGENT]` | `/feature` **settings** — theme persist + crash-save toggle. Spec: [`docs/features/settings.md`](features/settings.md) |
| 🔲 | AD.3 | `[AGENT]` | `/feature` **crash-capture** — opt-in local queue; never auto-send. Spec: [`docs/features/crash-capture.md`](features/crash-capture.md) |
| 🔲 | AD.4 | `[AGENT]` | `/feature` **feedback** — in-app feedback dialogs. Spec: [`docs/features/feedback.md`](features/feedback.md) |
| 🔲 | AD.5 | `[AGENT]` | `/feature` **github-feedback** — `ACTION_VIEW` issue form URL. Spec: [`docs/features/github-feedback.md`](features/github-feedback.md) |
| 🔲 | AD.6 | `[AGENT]` | `/feature` **privacy-report** — on-device sanitizer. Spec: [`docs/features/privacy-report.md`](features/privacy-report.md) |
| 🔲 | AD.7 | `[AGENT]` | `/feature` **display-refresh** — safe refresh-mode fallback. Spec: [`docs/features/display-refresh.md`](features/display-refresh.md) |
---

## Milestone Y leftovers

Human items stay open until cleared. See [`HUMAN_BACKLOG.md`](../HUMAN_BACKLOG.md).

### Human & device (after automation)

| Status | ID | Label | Task |
|--------|----|-------|------|
| 🔲 | Y.H1 | `[HUMAN]` | Confirm branch protection still requires **Android CI** + **CodeQL** (no silent rename) |
| 🔲 | Y.H2 | `[HUMAN]` | Review FOSS Cursor hooks (encoding + shell denylist) after first Agent session |
| 🔲 | Y.H3 | `[HUMAN]` | Triage Dependabot PRs (#27-#32 / successors) via `triage-dependabot-prs.sh --apply` |
| 🔲 | Y.H4 | `[HUMAN]` | Push any unpushed security workflows if still local-only |
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
