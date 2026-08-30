# Feature: repair-missing-packages

> One-tap remove uninstalled apps from groups. BUILD_PLAN **AE.13**.

## Acceptance criteria

- ✅ `GroupRepair.pruneUninstalled` filters group app lists against installed packages
- ✅ `GroupRepair.countMissingPackages` calculates total missing references
- ✅ Clean preservation of group metadata during prune
- ✅ Offline calculation

## Tests

- Automated: `GroupRepairTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
