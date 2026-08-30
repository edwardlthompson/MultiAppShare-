#!/usr/bin/env bash
# Multi App Share — bootstrap validation (android child repo profile)
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

QUICK=false
for arg in "$@"; do
  case "$arg" in
    --quick) QUICK=true ;;
  esac
done

REQUIRED=(
  README.md
  LICENSE
  CONTRIBUTING.md
  SECURITY.md
  CODE_OF_CONDUCT.md
  docs/BUILD_PLAN.md
  AGENTS.md
  AGENT_MEMORY.md
  docs/DECISION_LOG.md
  docs/COMPLETED_TASKS.md
  HUMAN_BACKLOG.md
  TEMPLATE_INDEX.json
  .template-version
  .template-update.json
  docs/START_HERE.md
  docs/CURSOR_MODES.md
  docs/FOR_AGENTS.md
  docs/INITIALIZATION_PROMPT.md
  docs/BOOTSTRAP_TEMPLATE_MAP.md
  docs/BOOTSTRAP_ALIGNMENT.md
  docs/UPGRADING_FROM_TEMPLATE.md
  docs/BEST_PRACTICES.md
  docs/FIRST_30_DAYS.md
  docs/AGENT_PORTABILITY.md
  docs/BOOTSTRAP_SYNC_0.21.md
  CLAUDE.md
  GEMINI.md
  CONVENTIONS.md
  bootstrap.config.json
  .cursor/rules/main.mdc
  scratchpad.md.example
  .cursor/rules/cursor-modes.mdc
  .cursor/rules/batch-commands.mdc
  .cursor/rules/local-compute.mdc
  .cursor/rules/project.mdc
  .cursor/hooks.json
  docs/DESIGN_GUIDE.md
  docs/GOLDEN_PATH.md
  docs/SECURITY_TRIAGE.md
  docs/THREAT_MODEL.md
  docs/PRIVACY.md
  docs/RUNBOOK.md
  docs/FEATURE_MODULES.md
  docs/GATES.md
  .github/dependabot.yml
  CODEOWNERS
  THIRD_PARTY_LICENSES.md
  .env.example
  docs/help/BATCH_COMMANDS.md
  docs/help/UPGRADE.md
  docs/help/ADR.md
  docs/help/ALLIDEAS.md
  docs/help/DEBUG.md
  docs/help/batch-commands-print.html
  docs/BATCH_COMMANDS.md
  CODE_REVIEW.md.example
  RELEASE_NOTES.md.example
  schemas/batch-commands-print.json
  docs/features/_handoff.md
  .cursor/rules/local-deps.mdc
  modules/android/MODULE.md
  gradlew
  app/build.gradle.kts
)

BATCH_COMMANDS=(
  audit cleanup debug gates triage dependabot push prerelease regress
  feature fix init prune ci docs upgrade setup plan restore compact scope
  codex-review coach tour ideas allideas update-deps best-of-n emulator adr
  bootstrap verify build ship maintain
)

for cmd in "${BATCH_COMMANDS[@]}"; do
  REQUIRED+=(".cursor/commands/${cmd}.md")
done

ERRORS=0

run_check() {
  if ! "$@"; then
    ERRORS=$((ERRORS + 1))
  fi
}

for f in "${REQUIRED[@]}"; do
  if [ ! -e "$f" ]; then
    echo "MISSING: $f"
    ERRORS=$((ERRORS + 1))
  fi
done

if [ -f LICENSE ] && [ ! -s LICENSE ]; then
  echo "EMPTY: LICENSE"
  ERRORS=$((ERRORS + 1))
fi

if ! grep -qE '\[(AGENT|HUMAN|ADB)\]' docs/BUILD_PLAN.md; then
  echo "MISSING: docs/BUILD_PLAN.md owner labels"
  ERRORS=$((ERRORS + 1))
fi

bash scripts/sync-exemplar-config.sh >/dev/null 2>&1 || true

# Independent read-only checks — local CPU (BOOTSTRAP_CHECK_JOBS overrides)
if ! python3 scripts/lib/run_checks_parallel.py \
  check-file-encoding.sh \
  check-markdown-tables.sh \
  check-changelog-unreleased.sh \
  check-repo-hygiene.sh \
  check-batch-commands.sh \
  check-cursor-hooks.sh \
  check-template-version-sync.sh \
  validate-template-index.sh
then
  ERRORS=$((ERRORS + 1))
fi

# BUILD_PLAN parallel structure (defaults to docs/BUILD_PLAN.md)
run_check bash scripts/check-build-plan-parallel.sh --draft "$ROOT/docs/BUILD_PLAN.md"

if [ "$QUICK" = false ]; then
  run_check bash scripts/check-readme-health.sh
fi

if [ "$ERRORS" -gt 0 ]; then
  echo "$ERRORS bootstrap check(s) failed"
  exit 1
fi

if [ "$QUICK" = true ]; then
  echo "Bootstrap validation passed (--quick)"
else
  echo "Bootstrap validation passed"
fi
