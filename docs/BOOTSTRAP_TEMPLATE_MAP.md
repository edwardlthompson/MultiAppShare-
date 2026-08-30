# Bootstrap Template Map - Multi App Share

Maps [agent-project-bootstrap](https://github.com/edwardlthompson/agent-project-bootstrap) v**1.0.0** to this **android** child repo.

**Production path locked:** root Gradle modules - do not relocate to `examples/android/`.

**Alignment record:** [`BOOTSTRAP_ALIGNMENT.md`](BOOTSTRAP_ALIGNMENT.md)

## Root documentation

| Template path | MultiAppShare path | Notes |
|---------------|-------------------|-------|
| `BUILD_PLAN.md` | `docs/BUILD_PLAN.md` | Active sprint board |
| `AGENTS.md` | `AGENTS.md` | Agent router |
| `AGENT_MEMORY.md` | `AGENT_MEMORY.md` | Living memory |
| `COMPLETED_TASKS.md` | `docs/COMPLETED_TASKS.md` | Milestone archive |
| `DECISION_LOG.md` | `docs/DECISION_LOG.md` | Major trade-offs |
| `HUMAN_BACKLOG.md` | `HUMAN_BACKLOG.md` | Non-automatable items |
| `TEMPLATE_INDEX.json` | `TEMPLATE_INDEX.json` | Agent file index (paths adapted) |
| `docs/DESIGN_GUIDE.md` | Pointer to `docs/GOLDEN_PATH.md` + Material 3 in `:core-ui` | |
| `examples/android/` | `app/`, `feature-dashboard/`, `core-*` | Production modules |
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

## Cursor surface (0.15.0)

| Template | MultiAppShare |
|----------|---------------|
| `.cursor/rules/*.mdc` | Includes `local-compute.mdc` + `project.mdc` |
| `.cursor/commands/*.md` | 26 commands (21 atomic + 5 super), Android-tuned gates/ci/push |
| `.cursor/skills/` | 7 skills (paths to `docs/BUILD_PLAN.md`) |
| `.cursor/agents/` | explorer, gate-fixer, verifier |
| `.cursor/hooks.json` | FOSS hooks enabled (encoding + shell guard) |
| Commercial examples | `*.commercial.example` only - not activated |

## Intentionally omitted

- `examples/web/`, `examples/python/`, `examples/node/`
- GitHub Pages / PWA workflows
- release-please (signed APK via `android.yml`)
- Relocating modules into `examples/`

## Intentional divergences

| Template | MultiAppShare |
|----------|---------------|
| Root `BUILD_PLAN.md` | `docs/BUILD_PLAN.md` |
| Root `DECISION_LOG.md` | `docs/DECISION_LOG.md` |
| `ci.yml` | `android.yml` (**Android CI**) |
| `CODEOWNERS` in `.github/` | `CODEOWNERS` at repo root |
| Generic file limits script | `check-file-limits.ps1` + bash wrapper |
| Milestone U (0.11.0) | Milestone **Y** (0.15.0, 2026-07-21) |

## Sync 0.21.0 (2026-08-20)

| Template | MultiAppShare |
|----------|---------------|
| Coach layer (`/coach`, `/tour`, `/ideas`) | Ported commands + `docs/help/*` + BEST_PRACTICES / FIRST_30_DAYS |
| Multi-agent adapters | `CLAUDE.md`, `GEMINI.md`, `CONVENTIONS.md`, `.cursor/rules/main.mdc`, `.agent/`, `.windsurf/`, `.continue/`, `.clinerules/` |
| `bootstrap.config.json` | Present (`stack: android`) |
| `verify.sh` | Present (env + validate-bootstrap) |
| `project.mdc` | **Preserved** alongside `main.mdc` |

## Sync 1.0.0 (2026-08-30)

| Template | MultiAppShare |
|----------|---------------|
| `/upgrade` + `docs/help/UPGRADE.md` | Child catch-up plan (Canon/Mixed/Sacred/Golden Path) |
| New atomics | `/adr`, `/allideas`, `/best-of-n`, `/emulator`, `/update-deps` |
| Security workflow | Trivy + Gitleaks + Semgrep FOSS + **Security Scan** rollup |
| Child gates | `validate-bootstrap.sh` stays android-child; `feature-gate.sh` keeps root `gradlew` |
| Not adopted | `ci.yml`, release-please, GitHub Pages, `examples/` overwrite |
