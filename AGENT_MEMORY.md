# Agent memory index — Multi App Share

Central index for persistent context. Append at milestone boundaries only.

## Project dimensions

| Dimension | Value |
|-----------|-------|
| **Platform** | Native Android — Kotlin 2.4.0, Jetpack Compose, Hilt 2.59.2, Room 2.8.4, Gradle 9.5.1 / AGP 9.2.1 |
| **Purpose** | Sequential multi-app sharing via custom groups; no backend, no accounts |
| **Distribution** | MIT; [GitHub Releases](https://github.com/edwardlthompson/MultiAppShare-/releases); [F-Droid](https://f-droid.org/packages/com.edwardlthompson.multiappshare/) via [`metadata/com.edwardlthompson.multiappshare.yml`](metadata/com.edwardlthompson.multiappshare.yml) |
| **App ID** | `com.edwardlthompson.multiappshare` |
| **Out of scope** | Wear OS, Android TV, web client, GMS/Firebase |
## Module map

`:core-domain` → `:core-database`, `:core-ui` → `:feature-dashboard` → `:app`

See [`docs/MODULE_BOUNDARIES.md`](docs/MODULE_BOUNDARIES.md).

## Security and privacy

- [`docs/THREAT_MODEL.md`](docs/THREAT_MODEL.md) — data at rest, export-only egress, no network exfiltration
- [`docs/BACKUP_FORMAT.md`](docs/BACKUP_FORMAT.md) — AES-256-GCM encrypted JSON exports
- [`docs/BACKUP_AND_CLOUD.md`](docs/BACKUP_AND_CLOUD.md) — Auto Backup vs manual export

## Platform compliance

- [`docs/TARGET_SDK_REVIEW.md`](docs/TARGET_SDK_REVIEW.md) — `compileSdk` **37**, `targetSdk` **36**
- [`docs/NATIVE_16KB_PAGE_SIZE.md`](docs/NATIVE_16KB_PAGE_SIZE.md) — native `.so` alignment
- [`docs/REPRODUCIBLE_BUILDS.md`](docs/REPRODUCIBLE_BUILDS.md) — SOURCE_DATE_EPOCH, F-Droid parity

## Build and release

- CI: [`.github/workflows/android.yml`](.github/workflows/android.yml)
- Local release: [`docs/LOCAL_RELEASE_BUILD.md`](docs/LOCAL_RELEASE_BUILD.md)
- F-Droid playbook: [`FDROID_MAINTENANCE.md`](FDROID_MAINTENANCE.md)

## Living plans

- Active roadmap: [`docs/BUILD_PLAN.md`](docs/BUILD_PLAN.md) — **AC** donations + GitHub APK updates (2026-08-20); bootstrap sync **0.21.0**; prior A–Y in COMPLETED_TASKS / DECISION_LOG
- Completed milestones A–X: [`docs/COMPLETED_TASKS.md`](docs/COMPLETED_TASKS.md)
- ADRs: [`docs/adr/README.md`](docs/adr/README.md)
- Decision log: [`docs/DECISION_LOG.md`](docs/DECISION_LOG.md)
- Human backlog: [`HUMAN_BACKLOG.md`](HUMAN_BACKLOG.md)

## Bootstrap agent workspace (v0.21.0)

| Artifact | Path |
|----------|------|
| Entry | [`docs/START_HERE.md`](docs/START_HERE.md) |
| Cursor modes | [`docs/CURSOR_MODES.md`](docs/CURSOR_MODES.md) |
| Slash commands (human) | [`docs/help/BATCH_COMMANDS.md`](docs/help/BATCH_COMMANDS.md) |
| Slash commands (agent) | [`docs/BATCH_COMMANDS.md`](docs/BATCH_COMMANDS.md) |
| Template map | [`docs/BOOTSTRAP_TEMPLATE_MAP.md`](docs/BOOTSTRAP_TEMPLATE_MAP.md) |
| Sync record | [`docs/BOOTSTRAP_SYNC_0.21.md`](docs/BOOTSTRAP_SYNC_0.21.md) |
| Alignment | [`docs/BOOTSTRAP_ALIGNMENT.md`](docs/BOOTSTRAP_ALIGNMENT.md) |
| Template index | [`TEMPLATE_INDEX.json`](TEMPLATE_INDEX.json) |
| Config | [`bootstrap.config.json`](bootstrap.config.json) · [`.template-version`](.template-version) |
| Cursor rules | `.cursor/rules/*.mdc` (includes `local-compute.mdc`, `main.mdc`, `commercial-compliance.mdc`) |
| Cursor commands | `.cursor/commands/*.md` (30 files: 25 atomic + 5 super) |
| Skills / agents / hooks | `.cursor/skills/`, `.cursor/agents/`, `.cursor/hooks.json` |
| Adapters | `CLAUDE.md`, `GEMINI.md`, `CONVENTIONS.md`, `.agent/`, `.windsurf/`, `.continue/`, `.clinerules` |
| Verify | [`scripts/verify.sh`](scripts/verify.sh) |
| Gate scripts | `scripts/validate-bootstrap.sh`, `watch-agent-gates.sh`, `feature-gate.sh`, `verify.sh` |
| Template version | `.template-version` → **0.21.0** |
## Retrospectives

### Bootstrap sync 0.21.0 (2026-08-20)

- Closed gaps from 0.15 → upstream HEAD: coach/tour/ideas/codex-review, multi-agent adapters, `verify.sh`, `bootstrap.config.json`, `docs/features/_handoff.md`.
- Gates: `validate-bootstrap.sh --quick` + `verify.sh` + encoding — pass.
- Preserved: `project.mdc`, `docs/` boards, `android.yml`, no release-please.
- Uncommitted until human asks; working tree may also include unrelated high-refresh display changes — keep separate commit.

### Milestone Y (2026-07-21)

- Aligned agent surface to bootstrap **0.15.0**: skills, FOSS hooks, agents, `local-compute`, `/cleanup`, TEMPLATE_INDEX, upgrade checker.
- Kept intentional divergences: board under `docs/`, `android.yml` (no release-please).
- Added `dependency-review.yml`, `stale.yml`, android-adapted `weekly-health-check.yml`.
- HUMAN remaining: branch protection confirm, hooks UX review, Dependabot triage, push when approved.

### Milestone X (2026-07-12)

- /audit: fixed CRLF shell scripts + *.sh eol=lf; Windows gh.exe resolution; docs now name **Android CI** + **CodeQL** (Scorecard/Trivy optional).
- HUMAN remaining: Dependabot PRs #27–#32, JAVA_HOME for agent Git Bash Gradle, optional Scorecard adoption.
