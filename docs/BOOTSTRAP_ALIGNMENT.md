# Bootstrap Alignment - Multi App Share

> Gap analysis and migration record: agent-project-bootstrap **0.11.0 -> 0.15.0**.
> Written 2026-07-21. Active sprint: Milestone **Y** in [`BUILD_PLAN.md`](BUILD_PLAN.md).

## Stack selection

| Dimension | Choice |
|-----------|--------|
| Profile | Android child (FOSS) |
| Production path | Root Gradle (`app/`, `feature-*`, `core-*`) - not `examples/android/` |
| License | MIT (unchanged) |
| Omitted stacks | web, python, node, go, rust, Lightroom |

## What already matched (0.11.0)

- Agent entrypoints: `AGENTS.md`, `docs/START_HERE.md`, `docs/CURSOR_MODES.md`, `docs/FOR_AGENTS.md`
- Slash commands (25) + `docs/help/BATCH_COMMANDS.md` + `batch-commands.mdc`
- Core `.cursor/rules/*.mdc` (except `local-compute.mdc`)
- Gate scripts: `validate-bootstrap`, `feature-gate`, `watch-agent-gates`, hygiene/encoding
- Security policy: `SECURITY.md`, `SECURITY_TRIAGE.md`, Dependabot, CodeQL, Trivy, Scorecard
- Child map: [`BOOTSTRAP_TEMPLATE_MAP.md`](BOOTSTRAP_TEMPLATE_MAP.md)
- Memory: `AGENT_MEMORY.md`, `docs/DECISION_LOG.md`, `docs/COMPLETED_TASKS.md`

## Gaps vs upstream 0.15.0

| Area | Gap |
|------|-----|
| Cursor runtime | skills, hooks, agents, `local-compute.mdc`, cleanup command, permissions/worktrees JSON |
| Process files | `TEMPLATE_INDEX.json`, `.template-update.json`, `UPGRADING_FROM_TEMPLATE.md`, `HUMAN_BACKLOG.md` |
| CI | `dependency-review.yml` documented but absent; optional stale/weekly-health |
| Scripts | template update/sync peers; parallel check runner |
| BUILD_PLAN | No Sequential/Parallel/Human-device lanes; no emoji-only active status protocol |

## Intentional divergences (preserved)

| Template | MultiAppShare |
|----------|---------------|
| Root `BUILD_PLAN.md` | `docs/BUILD_PLAN.md` |
| Root `DECISION_LOG.md` / `COMPLETED_TASKS.md` | under `docs/` |
| `ci.yml` | `android.yml` (Android CI) |
| release-please / Pages | Not adopted - signed APK via `android.yml` |
| Commercial Bugbot/cloud MCP | Examples only; not activated |

## Risk areas

- FOSS Cursor hooks change agent shell behavior (encoding + denylist)
- New workflows must not rename required checks (Android CI, CodeQL)
- Template version sync must work without release-please manifest

## Migration notes (Milestone Y close)

### What changed (agent/process only)

1. Template version **0.11.0 -> 0.15.0** (`.template-version`, `TEMPLATE_INDEX.json`, `.template-update.json`).
2. Cursor surface: `local-compute.mdc`, `/cleanup`, 7 skills, 3 agents, FOSS `hooks.json`, permissions/worktrees JSON.
3. Scripts: android-child `sync-template-version` / `check-template-version-sync`, `check-template-updates`, `agent-run.py`, parallel check runner.
4. CI additives: `dependency-review.yml`, `stale.yml`, android-adapted `weekly-health-check.yml` (does not rename **Android CI**).
5. Process: `HUMAN_BACKLOG.md`, emoji BUILD_PLAN Milestone Y, `docs/UPGRADING_FROM_TEMPLATE.md`, refreshed map + this alignment record.

### What still needs manual attention

| ID | Owner | Action |
|----|-------|--------|
| Y.H1 | `[HUMAN]` | Confirm branch protection requires **Android CI** + **CodeQL** |
| Y.H2 | `[HUMAN]` | Review FOSS Cursor hooks after first Agent session |
| Y.H3 | `[HUMAN]` | Triage Dependabot PRs via `triage-dependabot-prs.sh --apply` |
| Y.H4 | `[HUMAN]` | Push this alignment when ready (explicit approval) |
| Y.A1 | `[ADB]` | Device smoke - N/A unless UX touched (process-only sprint) |

### Intentionally not adopted

- release-please / Pages / renaming `android.yml` to `ci.yml`
- Commercial Bugbot/cloud MCP activation
- Copying `examples/web`, `examples/python`, `examples/node`
