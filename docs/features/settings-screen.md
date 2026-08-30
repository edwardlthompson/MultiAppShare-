# Feature: settings-screen

> Dedicated settings screen domain model and section definitions to declutter overflow menu. BUILD_PLAN **AE.36**.

## Acceptance criteria

- ✅ `SettingsCatalog.ENTRIES` defines sections for APPEARANCE, SHARING_BEHAVIOR, DATA_MANAGEMENT, and ABOUT
- ✅ `SettingsCatalog.filterBySection` partitions settings cleanly
- ✅ Pure logic data structures

## Tests

- Automated: `SettingsCatalogTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
