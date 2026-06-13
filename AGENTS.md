# Agent instructions — Multi App Share

> **Update rule:** Change this file only at session startup, milestone boundaries, or major architectural pivots. See also [`AGENT_MEMORY.md`](AGENT_MEMORY.md), [`KNOWLEDGE_BASE.md`](KNOWLEDGE_BASE.md), and [`docs/BUILD_PLAN.md`](docs/BUILD_PLAN.md).

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

## Plan mode and critique

For non-trivial work, use Plan Mode. Plans must include a **Critique** subsection covering null/empty inputs, network timeouts (N/A for this app), concurrency, and unhandled exceptions.

## Parallel agent guardrails

- Branch: `feature/agent-<task-name>` in an isolated worktree.
- **Never** assign two agents overlapping file boundaries.
- Room schema / shared API types: **one sequential agent** before parallel feature work.
- Instrumented tests stay **local/device** — not in default CI (see **P.7**).

## Session checkpoint

Before a major milestone or when context is degraded:

1. Write `.cursor-session-state` with completion status, open bugs, next steps.
2. Ask the user to start a fresh chat.
3. On restart, read `.cursor-session-state` via `@` indexing, then delete it.

## 3-strike rule

If the same build/test failure persists after **3 consecutive code changes**, stop. Summarize the conflict, list failed approaches, and ask the human for direction.

## Quality gates (local)

```bash
./gradlew lint test detekt koverXmlReport assembleDebug
```

Paparazzi goldens: `./gradlew :app:recordPaparazziDebug` when UI changes intentionally.

## Pre-release

Run [`docs/PRE_RELEASE_AUDIT.md`](docs/PRE_RELEASE_AUDIT.md) before every `v*` tag.

## Branch protection (human)

Maintainer must enable required status checks (`build`, CodeQL) and linear history on `main` in GitHub Settings. Documented in [`CONTRIBUTING.md`](CONTRIBUTING.md).
