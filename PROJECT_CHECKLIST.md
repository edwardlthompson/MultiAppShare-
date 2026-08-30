# Project Checklist

> Status: 🔲 open · ✅ done · ❌ blocked.
> Project: **Multi App Share** · Stack: `android` · License: `MIT`

## Setup

- ✅ README updated with value proposition and quickstart
- ✅ Environment variables configured (`.env.example` mirrored; `.env` not committed)
- ✅ Initial tests passing in the local environment
- 🔲 Pre-commit hooks installed (`pre-commit install`) — `[HUMAN]` optional

## Security & CI (defaults on)

- ✅ CI workflow verified on GitHub (required check: **Android CI**)
- ✅ CodeQL enabled (Actions language until Kotlin 2.4 is supported)
- ✅ Dependabot alerts enabled
- 🔲 Branch protection still requires **Android CI** + **CodeQL** — `[HUMAN]` Y.H1
- ✅ `SECURITY.md` reporting channel confirmed

## Agent adapters

- ✅ `AGENTS.md` reviewed for this product (Sacred — do not overwrite from template)
- ✅ Adapters current (`bash scripts/bootstrap-lifecycle.sh --sync-adapters`)
  - `.cursor/rules/main.mdc`
  - `CLAUDE.md`
  - `.github/copilot-instructions.md`

## Next

1. `python3 scripts/agent-run.py validate-bootstrap --quick`
2. `python3 scripts/agent-run.py feature-gate --stack android`
3. Say Golden Path numbers to add BUILD_PLAN rows (do not implement until named)
