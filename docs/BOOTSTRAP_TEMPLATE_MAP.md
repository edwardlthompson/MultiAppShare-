# Bootstrap Template Map — Multi App Share

Maps [agent-project-bootstrap](https://github.com/edwardlthompson/agent-project-bootstrap) v**0.11.0** to this **android** child repo.

**Production path locked:** root Gradle modules — do not relocate to `examples/android/`.

## Root documentation

| Template path | MultiAppShare path | Notes |
|---------------|-------------------|-------|
| `BUILD_PLAN.md` | `docs/BUILD_PLAN.md` | Active sprint board |
| `AGENTS.md` | `AGENTS.md` | Agent router |
| `AGENT_MEMORY.md` | `AGENT_MEMORY.md` | Living memory |
| `COMPLETED_TASKS.md` | `docs/COMPLETED_TASKS.md` | Milestone archive |
| `DECISION_LOG.md` | `docs/DECISION_LOG.md` | Major trade-offs |
| `docs/DESIGN_GUIDE.md` | Pointer → `docs/GOLDEN_PATH.md` + Material 3 in `:core-ui` | |
| `examples/android/` | **`app/`**, **`feature-dashboard/`**, **`core-*`** | Production modules |
| `docs/GATES.md` | `docs/GATES.md` | Release + milestone gate log |

## Golden Path

| Template concept | MultiAppShare production |
|------------------|--------------------------|
| Gradle / Kotlin | Root `gradlew`, `app/build.gradle.kts`, module `build.gradle.kts` |
| Compose UI | `app/.../ui/`, `feature-dashboard/` |
| Domain logic | `core-domain/`, use cases in `app/` |
| Persistence | `core-database/` (Room) |
| Unit tests | `*/src/test/` |
| F-Droid | `metadata/`, `fastlane/` |
| Feature template | [`docs/GOLDEN_PATH.md`](GOLDEN_PATH.md) (encrypted backup) |

## Gates

| Template script | MultiAppShare equivalent |
|-----------------|--------------------------|
| `validate-bootstrap.sh --quick` | `scripts/validate-bootstrap.sh` |
| `watch-agent-gates.sh` | `scripts/watch-agent-gates.sh` |
| `feature-gate.sh` | `./gradlew lint test detekt koverXmlReport assembleDebug` |
| `pre-release-gate.sh` | + [`docs/PRE_RELEASE_AUDIT.md`](PRE_RELEASE_AUDIT.md) |
| Device smoke | [`docs/RELEASE_SMOKE.md`](RELEASE_SMOKE.md), `:app:connectedDebugAndroidTest` |

## Cursor rules & commands

| Template | MultiAppShare |
|----------|---------------|
| `.cursor/rules/batch-commands.mdc` | ✅ 25 slash commands |
| `.cursor/rules/project.mdc` | Module boundaries, FOSS, BUILD_PLAN path |
| `.cursor/commands/*.md` | Customized `gates`, `prerelease`, `regress`, `ci`, `init`, `push` |

## Intentionally omitted

- `examples/web/`, `examples/python/`, `examples/node/`
- GitHub Pages / PWA workflows
- Relocating modules into `examples/`

## Intentional divergences

| Template | MultiAppShare |
|----------|---------------|
| Root `BUILD_PLAN.md` | `docs/BUILD_PLAN.md` |
| Root `DECISION_LOG.md` | `docs/DECISION_LOG.md` |
| `CODEOWNERS` in `.github/` | `CODEOWNERS` at repo root |
| Generic file limits script | `check-file-limits.ps1` + bash wrapper |
| Milestone R partial alignment | Milestone **U** full bootstrap (2026-06-19) |
