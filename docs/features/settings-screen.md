# Feature: settings-screen

> Dedicated settings screen domain model and searchable UI. BUILD_PLAN **AE.36** / **UX-008**.
>
> Layer: user-visible (`SettingsScreen`). Domain: `SettingsCatalog`.

## Acceptance criteria

- ✅ `SettingsCatalog.ENTRIES` defines sections for APPEARANCE, SHARING_BEHAVIOR, DATA_MANAGEMENT, and ABOUT
- ✅ `SettingsCatalog.filterBySection` partitions settings cleanly
- ✅ Pure logic data structures
- ✅ Full-screen Settings from overflow Settings; search uses `SettingsCatalog.search` plus localized titles
- ✅ Two-pane list + detail when width ≥ 600dp (`DesktopWindowingHelper`)
- ✅ Overflow no longer lists Theme / Language / Delay

## Tests

- Automated: `SettingsCatalogTest`, `SettingsInvokeTest`, `SettingsScreenPaparazziTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
