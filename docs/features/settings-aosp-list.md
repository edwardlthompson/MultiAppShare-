# Feature: settings-aosp-list

> Settings is a sectioned searchable list. BUILD_PLAN **UX-073**.

## Acceptance criteria

- ✅ `SettingsScreen` is a list with `SettingsSection` headers
- ✅ Search uses `SettingsCatalog.search`
- ✅ Catalog has Appearance, Sharing, Data, and About sections

## Tests

- Automated: `SettingsCatalogTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
