# Gates log — Multi App Share

Record milestone and release gate evidence here.

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
