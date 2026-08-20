# Start Here — Multi App Share

> **Read this file first** — whether you are a human or a Cursor agent.

## What is this?

**Multi App Share** is a native Android FOSS app for sequential multi-app sharing. This repo uses the [agent-project-bootstrap](https://github.com/edwardlthompson/agent-project-bootstrap) v**0.21.0** agent framework (sync 2026-08-20).

## Which repo mode are you in?

- **Reference (this repo):** Existing shipped Android project → read [`docs/CURSOR_MODES.md`](CURSOR_MODES.md), then [`docs/FOR_AGENTS.md`](FOR_AGENTS.md)
- **Bootstrap init only:** Run `/init` or `/bootstrap` — **do not** prune production modules

## Cursor modes (Plan / Agent / Debug / Ask)

See [`docs/CURSOR_MODES.md`](CURSOR_MODES.md) — pick the Cursor mode before editing code.

## Agent shortcuts

Type **`/`** in Cursor Agent chat. Start with **[docs/help/BATCH_COMMANDS.md](help/BATCH_COMMANDS.md)**:

| Command | When |
|---------|------|
| `/verify` | Before merge — docs + local gates |
| `/gates` | Local Gradle + bootstrap checks only |
| `/coach` | Why / best-practices coaching |
| `/tour` | First-run orientation |
| `/ideas` | Human backlog capture |
| `/prerelease` | Before every `v*` tag |
| `/ship` | Full release path (human push approval) |
| `/debug` | Defect investigation (not pre-release audit) |

## Read order (agents)

1. `README.md`
2. `docs/START_HERE.md` (this file)
3. `docs/CURSOR_MODES.md`
4. `AGENTS.md`
5. `docs/BUILD_PLAN.md` Sequential lane
6. `docs/BOOTSTRAP_TEMPLATE_MAP.md` (template → this repo)
7. `docs/BOOTSTRAP_ALIGNMENT.md` (migration / sync notes)
8. Active module docs: `docs/MODULE_BOUNDARIES.md`, `docs/GOLDEN_PATH.md`
9. `KNOWLEDGE_BASE.md` when debugging edge cases

## Do not read yet

- Inactive bootstrap `examples/` stacks (not present in this repo)
- `docs/MAINTAINING_THE_TEMPLATE.md` (template maintainer only)

## BUILD_PLAN labels

`[AGENT]` | `[HUMAN]` | `[ADB]` | `[AUTO]` — filter with `grep '\[AGENT\]' docs/BUILD_PLAN.md`

## Security

Dependabot + CodeQL enabled. Weekly triage: [`docs/SECURITY_TRIAGE.md`](SECURITY_TRIAGE.md). Report vulnerabilities: [`SECURITY.md`](../SECURITY.md).

## Agent prompt (copy-paste)

**Reference:** Read @docs/START_HERE.md, @docs/CURSOR_MODES.md, and @AGENTS.md. Pick Cursor mode per CURSOR_MODES. Follow docs/BUILD_PLAN.md Sequential lane. Run `/verify` before marking tasks complete.
