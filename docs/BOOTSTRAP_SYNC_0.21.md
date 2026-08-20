# Bootstrap sync — 0.15.0 to 0.21.0 (2026-08-20)

Gap analysis vs [agent-project-bootstrap](https://github.com/edwardlthompson/agent-project-bootstrap) HEAD (`0.21.0`).

## Already current

- Core entrypoints: `AGENTS.md`, `docs/START_HERE.md`, `CURSOR_MODES`, `FOR_AGENTS`
- FOSS hooks, 7 prior skills, agents, `TEMPLATE_INDEX`, `HUMAN_BACKLOG`, `.template-update.json`
- CI additives: dependency-review, stale, weekly-health, Scorecard, Security Scan, CodeQL
- `local-compute.mdc`, `/cleanup`, hygiene/encoding gates
- Android primary CI: `android.yml` (intentional)

## Outdated (updated this sync)

- `.template-version` 0.15.0 → **0.21.0**
- Batch registries → 25 atomics + 5 supers (coach, tour, ideas, codex-review)
- `AGENTS.md` / `START_HERE` / map / UPGRADING pointers for coach layer
- `validate-bootstrap` required files + command list

## Missing (added this sync)

- Commands/skill: coach, tour, ideas, codex-review
- Docs: BEST_PRACTICES, FIRST_30_DAYS, AGENT_PORTABILITY, CODEX_REVIEW, help/{COACH,TOUR,IDEAS,GLOSSARY,CURSOR_FEATURES}, `docs/features/_handoff.md` (+ `_template.md`)
- Adapters: CLAUDE.md, GEMINI.md, CONVENTIONS.md, main.mdc, .agent / .windsurf / .continue / .clinerules
- Scripts: verify.sh, check-env, check-agent-adapters, check-doc-links, check-conventional-commit, run-codex-review, bootstrap-lifecycle, project-health, simulate-template-upgrade
- `bootstrap.config.json`, `env.schema.json`, `scratchpad.md.example`, `commercial-compliance.mdc`

## Intentionally preserved

- `project.mdc` (android product rules)
- Board files under `docs/` (not root)
- `android.yml` (not `ci.yml`); no release-please / Pages
- Production Gradle at repo root (not `examples/android/`)
- Android-tuned `/gates`, `/push`, `/prerelease`, `/ci`
- Application modules and business logic

## Not applied automatically (manual follow-up)

| Item | Reason | Recommendation |
|------|--------|----------------|
| Rename CI to `ci.yml` | Would break branch protection | Keep `android.yml`; keep docs aligned |
| release-please | Conflicts with Android APK release path | Stay on tag + `android.yml` release-apk |
| Dependabot Compose/Android BOM bumps from template | Unrelated dep churn | Triage separately via `/dependabot` |
| Blind overwrite of `INITIALIZATION_PROMPT` | Sacred per UPGRADING | Human review if needed |
| Full template `validate-bootstrap.sh` | Demands web/python trees | Keep android-child profile |
