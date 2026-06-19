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

## Milestone W — Audit (2026-06-19)

| Step | Status | Notes |
|------|--------|-------|
| Local gates | ✅ | `/audit` — all scripts pass with JAVA_HOME |
| Actions pinned | ✅ | `gradle/actions/setup-gradle`, `action-semantic-pull-request` |
| Commit bootstrap | ✅ | 2026-06-19 |
| Dependabot enabled | ✅ | vulnerability alerts + security updates via `gh api` |
| Slash commands | ✅ | `verify-slash-commands.sh` — 25/25 files + registry |

## Historical

- **R.5.2** Release smoke — OnePlus CPH2583 (Android 16), v1.8.0 — archived in [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md)
