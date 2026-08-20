# Agent Router — Multi App Share

1. **First read:** [`docs/START_HERE.md`](docs/START_HERE.md)
2. **Cursor modes:** [`docs/CURSOR_MODES.md`](docs/CURSOR_MODES.md) (Ask / Plan / Agent / Debug)
3. **Why / coach:** [`docs/BEST_PRACTICES.md`](docs/BEST_PRACTICES.md) · [`docs/FIRST_30_DAYS.md`](docs/FIRST_30_DAYS.md) · `/coach` · backlog `/ideas` · first-run `/tour` · portability [`docs/AGENT_PORTABILITY.md`](docs/AGENT_PORTABILITY.md)
4. **Reference mode:** [`docs/FOR_AGENTS.md`](docs/FOR_AGENTS.md) + [`docs/BOOTSTRAP_TEMPLATE_MAP.md`](docs/BOOTSTRAP_TEMPLATE_MAP.md)
5. **Task board:** [`docs/BUILD_PLAN.md`](docs/BUILD_PLAN.md) (Sequential before Parallel) — status: open / done / blocked (emoji markers)
6. **Living memory:** update [`AGENT_MEMORY.md`](AGENT_MEMORY.md) only at milestone boundaries
7. **Slash commands:** type `/` in Agent chat — see [`docs/help/BATCH_COMMANDS.md`](docs/help/BATCH_COMMANDS.md)
8. **Bootstrap alignment:** [`docs/BOOTSTRAP_ALIGNMENT.md`](docs/BOOTSTRAP_ALIGNMENT.md) (template **v0.21.0**)

> Legacy `.cursorrules` is deprecated. Use `.cursor/rules/*.mdc` (including `main.mdc` + `project.mdc`), `.cursor/skills/`, FOSS `.cursor/hooks.json`, and this file.

<!-- bootstrap-project-card -->
**Product:** Multi App Share
**Purpose:** Native Android FOSS sequential multi-app sharing
**Stack:** android
<!-- /bootstrap-project-card -->

## Project summary

Native Android FOSS utility for sequential multi-app sharing. MIT license; GitHub Releases + F-Droid. No backend, no accounts, no proprietary SDKs.

## Architecture constraints

From [`CONTRIBUTING.md`](CONTRIBUTING.md) and [`docs/MODULE_BOUNDARIES.md`](docs/MODULE_BOUNDARIES.md):

1. **Inward dependency flow:** `:feature-*` → `:core-domain` / `:core-ui`; `:core-database` implements domain interfaces; `:core-domain` is pure Kotlin only.
2. **Strict encapsulation:** Use `internal` for concretions; expose interfaces/entities publicly.
3. **Context safety:** No Activity/Fragment leaks in ViewModels or repositories; use Hilt DI.
4. **FOSS only:** Zero `com.google.android.gms`, Firebase, or closed telemetry SDKs.
5. **File size limits:** Composable/view files ≤ **250 lines**; logic files ≤ **150 lines** (enforced by `scripts/check-file-limits.ps1`).

## Read-before-write

Before editing repositories, ViewModels, or persistence:

1. `@`-index interfaces and entities in `:core-domain` and `:core-database`.
2. Verify type signatures at boundaries (Room entities, backup wrappers, repository contracts).
3. Check [`docs/ROOM_MIGRATION_CHECKLIST.md`](docs/ROOM_MIGRATION_CHECKLIST.md) before bumping `@Database` version.

## Session protocol

- On session start: read `START_HERE.md`, pick mode via `CURSOR_MODES.md`, then `docs/BUILD_PLAN.md` Sequential lane
- On milestone end: update `AGENT_MEMORY.md`, append to `docs/DECISION_LOG.md` or `docs/adr/`
- After each major `[AGENT]` step: `bash scripts/watch-agent-gates.sh --once --autofix`
- On 3-strike failure: halt and escalate to human
- On context bloat: write `.cursor-session-state`, ask human to clear chat
- Destructive operations require `[HUMAN]` approval (see `.cursor/rules/destructive-ops.mdc`)

## Plan mode and critique

For non-trivial work, use Plan Mode. Plans must include a **Critique** subsection covering null/empty inputs, network timeouts (N/A for this app), concurrency, and unhandled exceptions.

## Parallel agent guardrails

- Branch: `feature/agent-<task-name>` in an isolated worktree.
- **Never** assign two agents overlapping file boundaries.
- Room schema / shared API types: **one sequential agent** before parallel feature work.
- Instrumented tests stay **local/device** — not in default CI (see **P.7**).

## Quality gates (local)

```bash
bash scripts/verify.sh
# or
bash scripts/validate-bootstrap.sh --quick
./gradlew lint test detekt koverXmlReport assembleDebug
bash scripts/feature-gate.sh --stack android
```

Paparazzi goldens: `./gradlew :app:recordPaparazziDebug` when UI changes intentionally.

Slash shortcuts: `/verify` (docs + gates + CI), `/ship` (prerelease + push + regress), `/coach` / `/tour` / `/ideas`.

## Pre-release

Run [`docs/PRE_RELEASE_AUDIT.md`](docs/PRE_RELEASE_AUDIT.md) before every `v*` tag. Prefer `/prerelease` or `/ship`.

## Branch protection (human)

Maintainer must enable required status checks (**Android CI**, CodeQL) and linear history on `main` in GitHub Settings. Documented in [`CONTRIBUTING.md`](CONTRIBUTING.md).

## Multi-agent adapters

Canonical spec is this file. After editing: `bash scripts/bootstrap-lifecycle.sh --sync-adapters` (or copy regenerated `CLAUDE.md` / `GEMINI.md` / `CONVENTIONS.md` / `.cursor/rules/main.mdc`). See [`docs/AGENT_PORTABILITY.md`](docs/AGENT_PORTABILITY.md).
