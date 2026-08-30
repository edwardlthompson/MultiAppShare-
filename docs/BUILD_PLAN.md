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

## Milestone Y leftovers

Human items stay open until cleared. See [`HUMAN_BACKLOG.md`](../HUMAN_BACKLOG.md).

### Human & device (after automation)

| Status | ID | Label | Task |
|--------|----|-------|------|
| 🔲 | Y.H1 | `[HUMAN]` | Confirm branch protection still requires **Android CI** + **CodeQL** (no silent rename) |
| 🔲 | Y.H2 | `[HUMAN]` | Review FOSS Cursor hooks (encoding + shell denylist) after first Agent session |
| 🔲 | Y.H4 | `[HUMAN]` | Push any unpushed security workflows if still local-only |
---

## Milestone AE — Product backlog (allideas 2026-08-30)

Child slices from `/allideas`. **One `/feature` task per row.** Sequential only until scopes are isolated. Skip leftovers already on the board (AC.3, Y.H1, Y.H2, Y.H4). **Never** copy `examples/android/` over the product.

<!-- parallel_exception: sequential product backlog; no isolated parallel scopes yet -->

| Status | ID | Label | Task |
|--------|----|-------|------|
| ✅ | AE.1 | `[AGENT]` | `/feature` **fdroid-aware-updates** — Detect F-Droid installer; listing only, never GitHub APK Install |
| ✅ | AE.2 | `[AGENT]` | `/feature` **payload-preview** — Show text/URI/MIME before the first sequential handoff |
| ✅ | AE.3 | `[AGENT]` | `/feature` **reorder-attachments** — Reorder `ACTION_SEND_MULTIPLE` URIs before share |
| ✅ | AE.4 | `[AGENT]` | `/feature` **pause-resume-share** — Pause and resume mid-sequence without Skip remaining |
| ✅ | AE.5 | `[AGENT]` | `/feature` **try-later-step** — Defer a failed step without counting it as skip |
| ✅ | AE.6 | `[AGENT]` | `/feature` **share-progress-live-region** — TalkBack announces step N of M and target app |
| ✅ | AE.7 | `[AGENT]` | `/feature` **reduce-motion-burst** — Honor reduced-motion / animator scale on success burst |
| ✅ | AE.8 | `[AGENT]` | `/feature` **haptics-toggle** — Settings toggle for sequential share haptics |
| ✅ | AE.9 | `[AGENT]` | `/feature` **per-group-delay** — Optional share delay override per group |
| ✅ | AE.10 | `[AGENT]` | `/feature` **mime-mismatch-sheet** — Explain why a group is hidden for the current MIME |
| ✅ | AE.11 | `[AGENT]` | `/feature` **share-step-deeplink** — Notification tap resumes the exact share step |
| ✅ | AE.12 | `[AGENT]` | `/feature` **qs-share-clipboard** — Quick Settings tile for clipboard share |
| ✅ | AE.13 | `[AGENT]` | `/feature` **repair-missing-packages** — One-tap remove uninstalled apps from groups |
| ✅ | AE.14 | `[AGENT]` | `/feature` **package-change-listener** — Refresh icons/groups on install or uninstall |
| ✅ | AE.15 | `[AGENT]` | `/feature` **group-editor-search** — Filter installed apps in the group editor |
| 🔲 | AE.16 | `[AGENT]` | `/feature` **duplicate-app-warning** — Warn when the same package is in two groups |
| 🔲 | AE.17 | `[AGENT]` | `/feature` **group-color-label** — Optional color or emoji label on a group |
| 🔲 | AE.18 | `[AGENT]` | `/feature` **group-notes** — Optional short description on a group |
| 🔲 | AE.19 | `[AGENT]` | `/feature` **pin-favorite-groups** — Manual pin above usage sort |
| 🔲 | AE.20 | `[AGENT]` | `/feature` **group-template-export** — Export one group as local JSON with no secrets |
| 🔲 | AE.21 | `[AGENT]` | `/feature` **import-merge-replace** — Choose merge vs replace on backup import |
| 🔲 | AE.22 | `[AGENT]` | `/feature` **autogroup-dry-run** — Preview auto-group buckets before commit |
| 🔲 | AE.23 | `[AGENT]` | `/feature` **hide-unused-apps** — Global hide list for the app picker |
| 🔲 | AE.24 | `[AGENT]` | `/feature` **sort-apps-last-success** — Order group apps by last successful share |
| 🔲 | AE.25 | `[AGENT]` | `/feature` **collapse-unused-overlay-groups** — Collapse groups with no MIME match |
| 🔲 | AE.26 | `[AGENT]` | `/feature` **history-retention** — 30/90-day local history prune |
| 🔲 | AE.27 | `[AGENT]` | `/feature` **clear-history-confirm** — Confirm before clearing history |
| 🔲 | AE.28 | `[AGENT]` | `/feature` **history-filter-group** — Filter history rows by group |
| 🔲 | AE.29 | `[AGENT]` | `/feature` **history-export-local** — Sanitized local JSON export of history |
| 🔲 | AE.30 | `[AGENT]` | `/feature` **history-open-uri** — Open original URI when persistable grant remains |
| 🔲 | AE.31 | `[AGENT]` | `/feature` **backup-payload-opt-in** — Toggle including last payload in backup |
| 🔲 | AE.32 | `[AGENT]` | `/feature` **passphrase-strength** — Strength meter on backup passphrase |
| 🔲 | AE.33 | `[AGENT]` | `/feature` **oss-license-screen** — In-app third-party license list |
| 🔲 | AE.34 | `[AGENT]` | `/feature` **privacy-policy-screen** — Scrollable local privacy screen |
| 🔲 | AE.35 | `[AGENT]` | `/feature` **backup-crash-pref-opt-in** — Optional backup of crash-save preference |
| 🔲 | AE.36 | `[AGENT]` | `/feature` **settings-screen** — Dedicated Settings screen (unclutter overflow) |
| 🔲 | AE.37 | `[AGENT]` | `/feature` **overflow-feedback** — Feedback entry on the overflow menu |
| 🔲 | AE.38 | `[AGENT]` | `/feature` **in-app-changelog** — Cached changelog readable offline |
| 🔲 | AE.39 | `[AGENT]` | `/feature` **settings-search** — Filter rows on the Settings screen |
| 🔲 | AE.40 | `[AGENT]` | `/feature` **monochrome-icon** — Android 13 themed / monochrome launcher icon |
| 🔲 | AE.41 | `[AGENT]` | `/feature` **battery-explainer** — User-initiated battery-optimization copy and link |
| 🔲 | AE.42 | `[AGENT]` | `/feature` **fgs-timeout** — Survive Android 15 foreground-service timeout |
| 🔲 | AE.43 | `[AGENT]` | `/feature` **notification-channel-link** — Deep link to system notification settings |
| 🔲 | AE.44 | `[AGENT]` | `/feature` **dynamic-shortcuts** — Rank launcher shortcuts by group usage |
| 🔲 | AE.45 | `[AGENT]` | `/feature` **home-widget** — Widget for last group or clipboard share |
| 🔲 | AE.46 | `[AGENT]` | `/feature` **foldable-two-pane** — Hinge / tabletop polish for two-pane |
| 🔲 | AE.47 | `[AGENT]` | `/feature` **desktop-windowing** — Freeform / window-size class overlay layout |
| 🔲 | AE.49 | `[AGENT]` | `/feature` **large-font-paparazzi** — 200% font overflow goldens for new dialogs |
| 🔲 | AE.50 | `[AGENT]` | `/feature` **locale-de-it** — German and Italian string parity |
| 🔲 | AE.51 | `[AGENT]` | `/feature` **rtl-pass** — RTL layout audit for overlay and arrows |
| 🔲 | AE.52 | `[AGENT]` | `/feature` **theme-toggle-labels** — Full-row TalkBack names on crash/refresh toggles |
| 🔲 | AE.53 | `[AGENT]` | `/feature` **about-install-source** — Show F-Droid vs sideload on About |
| 🔲 | AE.54 | `[AGENT]` | `/feature` **optional-emulator-ci** — Optional instrumented job when emulator exists |
| 🔲 | AE.55 | `[AGENT]` | `/feature` **kover-share-gaps** — Coverage report focus on share/retry path |
| 🔲 | AE.56 | `[AGENT]` | `/feature` **paparazzi-ad-dialogs** — Goldens for About, Feedback, crash review |
| 🔲 | AE.57 | `[AGENT]` | `/feature` **baseline-overlay** — Baseline profile for share overlay startup |
| 🔲 | AE.58 | `[AGENT]` | `/feature` **unreleased-ship-prep** — Version/changelog/fastlane prep for next `v*` |
| 🔲 | AE.60 | `[AGENT]` | `/feature` **foss-funding-links** — Liberapay or GitHub Sponsors beside Venmo |
| 🔲 | AE.61 | `[AGENT]` | `/feature` **repro-verify-prerelease** — Wire unsigned APK verify into `/prerelease` |
| 🔲 | AE.62 | `[AGENT]` | `/feature` **overflow-sections** — Group overflow into Share / Data / About |
| 🔲 | AE.63 | `[AGENT]` | `/feature` **import-confirm** — Confirm before destructive backup import |
| 🔲 | AE.64 | `[AGENT]` | `/feature` **empty-groups-cta** — Autofill vs create when the list is empty |
| 🔲 | AE.65 | `[AGENT]` | `/feature` **whats-new-local** — Offline what’s-new after version change |
| 🔲 | AE.66 | `[AGENT]` | `/feature` **work-profile-labels** — Disambiguate work vs personal share targets |
| 🔲 | AE.67 | `[AGENT]` | `/feature` **copy-group-names** — Copy group app list to clipboard |
| 🔲 | AE.68 | `[AGENT]` | `/feature` **app-info-shortcut** — Open system app-info from a group row |
| 🔲 | AE.69 | `[AGENT]` | `/feature` **group-last-share-time** — Show last-share timestamp on group rows |
| 🔲 | AE.70 | `[AGENT]` | `/feature` **crash-review-snooze** — Don’t ask again this version (still never auto-send) |

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
