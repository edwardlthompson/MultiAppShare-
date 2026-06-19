# Existing-repo bootstrap — Multi App Share

This repo was **not** created via **Use this template**. Run maintenance init only:

1. Confirm [`docs/BOOTSTRAP_TEMPLATE_MAP.md`](docs/BOOTSTRAP_TEMPLATE_MAP.md) — production path is root Gradle (`app/`, `core-*`, `feature-dashboard/`).
2. Verify `.template-version` and `.cursor/stack-selection.json` (`stack: android`).
3. Run (non-destructive):
   ```bash
   bash scripts/init-project.sh --stack android --non-interactive --no-prune \
     --project-name "Multi App Share" \
     --purpose "FOSS Android sequential multi-app sharing utility" \
     --release-repo edwardlthompson/MultiAppShare-
   ```
4. Run `bash scripts/validate-bootstrap.sh --quick` and `bash scripts/feature-gate.sh --stack android`.
5. Read [`docs/START_HERE.md`](docs/START_HERE.md) and [`docs/BUILD_PLAN.md`](docs/BUILD_PLAN.md) Sequential lane.

**Do not** prune `app/` or module directories.

Begin now.
