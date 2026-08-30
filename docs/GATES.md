# Gates log — Multi App Share

Record milestone and release gate evidence here.

## Milestone AD — Golden Path 1–7 (2026-08-30)

| Step | Result | Notes |
|------|--------|-------|
| Feature gate | ✅ | `feature-gate --stack android` (10 stages: hygiene, encoding, lint, unit tests, detekt, …) |
| Bootstrap | ✅ | `validate-bootstrap --quick` |
| File limits / encoding / hygiene | ✅ | `check-file-limits`; UTF-8; `check-repo-hygiene` |
| Device | ⏭️ | No emulator in this environment; AC.3 ADB leftover remains |
| Note | ℹ️ | Implemented in existing About/theme hosts; did not copy `examples/android/` over the product |

## Ship — v1.9.5 donations + updates (2026-08-22)

| Step | Result | Notes |
|------|--------|-------|
| Metadata sync | ✅ | `versionCode` 181 / `versionName` 1.9.5; F-Droid YAML; fastlane `181.txt`; CHANGELOG |
| `/prerelease` Gradle | ✅ | `lint test detekt koverXmlReport assembleDebug` |
| Bootstrap | ✅ | `validate-bootstrap --quick` |
| File limits / encoding / hygiene | ✅ | `check-file-limits.ps1 -Fail`; UTF-8; `check-repo-hygiene` |
| Dependabot | ✅ | 0 Critical/High |
| Codex | ⏭️ | SKIP — no `OPENAI_API_KEY` / Codex CLI |
| Device instrumented | ✅ | CPH2583 API 36; `/prerelease` **8/9** + ShareUxSmoke retry **1/1**; `/regress` **8/9** then **MilestoneAb** retry **2/2** |
| Paparazzi | ⏭️ | Windows host flake (empty-groups / create-group goldens); CI `ubuntu-latest` is source of truth |
| Tag | ✅ | `v1.9.5` @ `47f8f46` · [GitHub Release](https://github.com/edwardlthompson/MultiAppShare-/releases/tag/v1.9.5) |
| GitHub CI | ✅ | Android CI + CodeQL + Security Scan green @ `47f8f46`; tag `release-apk` uploaded `MultiAppShare-v1.9.5-release.apk` |
| `/regress` Gradle | ✅ | `lint test detekt koverXmlReport assembleDebug`; `validate-bootstrap --quick` |
| Note | ⚠️ | Auto donate/update prompts are **release-only** (`BuildConfig.DEBUG` skip) so instrumented tests stay quiet; menu donate still works on debug. Scorecard optional fail; TalkBack still `[HUMAN]`; WSL `pre-release-gate.sh` cannot read `gradle-wrapper.jar` |

## Ship — v1.9.4 Milestone AB (2026-08-20)

| Step | Result | Notes |
|------|--------|-------|
| Metadata sync | ✅ | `versionCode` 180 / `versionName` 1.9.4; F-Droid YAML; fastlane `180.txt`; CHANGELOG |
| `/prerelease` Gradle | ✅ | `lint test detekt koverXmlReport assembleDebug` |
| Bootstrap | ✅ | `validate-bootstrap.sh --quick` |
| File limits / encoding / hygiene | ✅ | `check-file-limits.ps1 -Fail`; UTF-8; `check-repo-hygiene` |
| Dependabot | ✅ | 0 Critical/High |
| Codex | ⏭️ | SKIP — no `OPENAI_API_KEY` / Codex CLI |
| Device instrumented | ✅ | SDK adb 1.0.41; `:app:connectedDebugAndroidTest` **9/9** on CPH2583 (API 36) including `MilestoneAbSmokeInstrumentedTest` |
| Paparazzi | ⏭️ | Windows host flake; CI `ubuntu-latest` is source of truth |
| Tag | 🔲 | `v1.9.4` (after push) |
| GitHub CI | 🔲 | After push |
| Note | ⚠️ | Scorecard optional; TalkBack still `[HUMAN]`; F-Droid GitLab MR leftover |

## Milestone AB — device smoke (2026-08-20)

| Step | Result | Notes |
|------|--------|-------|
| Device | ✅ | CPH2583 (OnePlus) API 36 · SDK adb 1.0.41 |
| AB.A1 instrumented | ✅ | `MilestoneAbSmokeInstrumentedTest` — filter with 2 groups, clipboard overlay, history row re-share (`sharingStarted=false`), fail broadcast stays on index 0 + Retry, backup v2 restores settings/last payload (v1 does not wipe theme), rename keeps `id` + `updateShortcuts` |
| Other connected | ✅ | `:app:connectedDebugAndroidTest` **9/9** including deeplink `id=` and `ShareUxSmoke` |
| Leftover | ⚠️ | Hardware TalkBack still `[HUMAN]` / `[USER-SKIP]` |

## Ship — v1.9.3 share/group UX (2026-08-20)

| Step | Result | Notes |
|------|--------|-------|
| Metadata sync | ✅ | `versionCode` 179 / `versionName` 1.9.3; F-Droid YAML; fastlane `179.txt`; CHANGELOG; APK `appVersion` matches |
| `/prerelease` Gradle | ✅ | `lint test detekt koverXmlReport assembleDebug` |
| Bootstrap | ✅ | `validate-bootstrap.sh --quick` |
| File limits / encoding | ✅ | `check-file-limits.ps1 -Fail`; UTF-8 |
| Dependabot | ✅ | 0 Critical/High |
| Codex | ⏭️ | SKIP — no `OPENAI_API_KEY` / Codex CLI |
| Device instrumented | ✅ | SDK adb 1.0.41 (PATH had Essential 1.0.39); `:app:connectedDebugAndroidTest` **6/6** on CPH2583 (API 36) |
| Device smoke (Z.A1 / AA.A1) | ✅ | Process-death + persistable URI resume; language/theme dialogs; tablet two-pane; skip / undo / merge |
| Paparazzi | ⏭️ | Windows host flake; CI `ubuntu-latest` is source of truth |
| Tag | ✅ | `v1.9.3` (versionCode 179) |
| GitHub CI | ✅ | Android CI + CodeQL + Security Scan green @ `fec647e` |
| Note | ⚠️ | Scorecard latest run failure (optional); TalkBack still `[HUMAN]` |

## Ship — v1.9.2 bootstrap 0.21 + high-refresh (2026-08-20)

| Step | Result | Notes |
|------|--------|-------|
| Metadata sync | ✅ | `versionCode` 178 / `versionName` 1.9.2; F-Droid YAML; fastlane `178.txt`; CHANGELOG |
| `/prerelease` | ✅ | `pre-release-gate.sh` — feature-gate 8/8; 0 Crit/High Dependabot |
| Codex | ⏭️ | SKIP — no `OPENAI_API_KEY` / Codex CLI |
| Device instrumented | ✅ | `:app:connectedDebugAndroidTest` 5/5 on CPH2583 (API 16) |
| Device smoke (manual) | ⏭️ | High-refresh is display-only; full `RELEASE_SMOKE.md` share/backup path unchanged |
| Paparazzi | ⏭️ | Windows host flake (0.02–0.05% empty-groups; overlay goldens differ vs Linux); CI `ubuntu-latest` is source of truth |
| Tag | ✅ | `v1.9.2` (versionCode 178) |
| Note | ⚠️ | Scorecard latest run failure (optional); Android CI + CodeQL expected after push |

## Pre-release (each `v*` tag)

| Gate | Owner | Evidence |
|------|-------|----------|
| Gradle + detekt + Kover | `[AUTO]` | `./gradlew lint test detekt koverXmlReport assembleDebug` |
| Paparazzi | `[AUTO]` | `./gradlew :app:verifyPaparazziDebug` |
| Bootstrap | `[AUTO]` | `bash scripts/validate-bootstrap.sh --quick` |
| Device smoke | `[ADB]` `[HUMAN]` | [`RELEASE_SMOKE.md`](RELEASE_SMOKE.md) |
| F-Droid metadata | `[HUMAN]` | [`FDROID_MAINTENANCE.md`](../FDROID_MAINTENANCE.md) |

## Milestone U — Template Migration (2026-06-19)

| Step | Status | Notes |
|------|--------|-------|
| U.1 `.cursor/` tracked | ✅ | `.gitignore` updated; `.template-version` 0.11.0 |
| U.2 Bootstrap scripts | ✅ | `validate-bootstrap.sh --quick` pass |
| U.3 Cursor rules | ✅ | 12 `.mdc` + `project.mdc` |
| U.4 Slash commands (25) | ✅ | `verify-slash-commands.sh` pass |
| U.5 Docs merge | ✅ | START_HERE, BOOTSTRAP_TEMPLATE_MAP, AGENTS router |
| U.6 Validation | ✅ | Gradle gate pass; Git Bash needs `JAVA_HOME` for `watch-agent-gates` |

**Gradle (2026-06-19):** `./gradlew lint test detekt koverXmlReport assembleDebug` — PASS (Android Studio JBR).

**Git Bash:** export `JAVA_HOME="/c/Program Files/Android/Android Studio/jbr"` before `bash scripts/watch-agent-gates.sh`.

## Ship — v1.9.1 bootstrap 0.15 (2026-07-21)

| Step | Result | Notes |
|------|--------|-------|
| Metadata sync | ✅ | `versionCode` 177 / `versionName` 1.9.1; F-Droid YAML; fastlane `177.txt`; CHANGELOG |
| `/prerelease` | ✅ | `pre-release-gate.sh` — feature-gate 8/8; 0 Crit/High Dependabot |
| Device smoke | ⏭️ | N/A — process/tooling only (no UX change) |
| Tag | ✅ | `v1.9.1` (versionCode 177) |
| Note | ⚠️ | Security Scan / Scorecard failed on prior HEAD (optional); Android CI + CodeQL green |

## Ship — bootstrap push (2026-06-19)

| Step | Result | Notes |
|------|--------|-------|
| `/prerelease` | ✅ | `pre-release-gate.sh`, `feature-gate` 8/8 stages |
| `/push` | ✅ | `6eb91e1` → `origin/main` (no new tag; v1.9.0 already released) |
| `/regress` | ✅ | `validate-bootstrap --quick`; CI green |
| Android CI | ✅ | [run 27850423758](https://github.com/edwardlthompson/MultiAppShare-/actions/runs/27850423758) |
| CodeQL | ✅ | [run 27850423756](https://github.com/edwardlthompson/MultiAppShare-/actions/runs/27850423756) |

Local note: `:app:verifyPaparazziDebug` flaky on Windows dev host; CI unit tests pass on Linux.

## Milestone V (2026-06-19)

| Task | Status | Notes |
|------|--------|-------|
| V.1 Configuration cache | ✅ | Full gate suite `--configuration-cache` pass |
| V.3b targetSdk 36 + E2E | ✅ | `enableEdgeToEdge()`; Gradle bump |
| V.3b Android 16 smoke | ✅ | CPH2583 (API 36) — details below |
| V.2 Dependabot triage | ✅ | 0 Critical/High alerts; merged #25, #23, #24 → `8afb342` — details below |

### V.3b device smoke (2026-06-19)

| Field | Value |
|-------|--------|
| Device | OnePlus **CPH2583** (wireless ADB) |
| OS | Android **16** · API **36** |
| Build | `MultiAppShare-v1.9.0-debug.apk` · `targetSdk=36` (`dumpsys package`) |
| Instrumented | **5/5** pass — `MainActivitySmokeInstrumentedTest`, `DeeplinkInstrumentedTest` |
| ADB cold launch | ✅ `Groups` top bar; no fatal |
| ADB share overlay | ✅ `SEND text/plain` → **Choose a group** |
| ADB rotation | ✅ landscape; overlay text preserved |
| ADB deeplinks | ✅ `multiappshare://open`; `multiappshare://group?name=…` |
| E2E insets | ✅ Compose content below status bar (y=384 on 1440×3168) |
| Deferred (pre-`v*` tag) | Full sequential handoff, failed-target skip, encrypted backup — [`RELEASE_SMOKE.md`](RELEASE_SMOKE.md) |

### V.2 Dependabot triage (2026-06-20)

| Field | Value |
|-------|--------|
| Critical/High alerts | **0** (`count-critical-high-dependabot.sh`) |
| Merged PRs | [#25](https://github.com/edwardlthompson/MultiAppShare-/pull/25) github-actions → `58fa7d5` · [#23](https://github.com/edwardlthompson/MultiAppShare-/pull/23) androidx → `c6418ab` · [#24](https://github.com/edwardlthompson/MultiAppShare-/pull/24) compose-bom → `8afb342` |
| Post-merge CI | ✅ [Android CI](https://github.com/edwardlthompson/MultiAppShare-/actions/runs/27870673145) · ✅ [CodeQL](https://github.com/edwardlthompson/MultiAppShare-/actions/runs/27870673138) on `8afb342` |

---

| Step | Status | Notes |
|------|--------|-------|
| Local gates | ✅ | `/audit` — all scripts pass with JAVA_HOME |
| Actions pinned | ✅ | `gradle/actions/setup-gradle`, `action-semantic-pull-request` |
| Commit bootstrap | ✅ | 2026-06-19 |
| Dependabot enabled | ✅ | vulnerability alerts + security updates via `gh api` |
| Slash commands | ✅ | `verify-slash-commands.sh` — 25/25 files + registry |

## Historical

- **R.5.2** Release smoke — OnePlus CPH2583 (Android 16), v1.8.0 — archived in [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md)
