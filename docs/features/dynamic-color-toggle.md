# Feature: dynamic-color-toggle

> Users can force the static Material 3 scheme when OEM wallpaper colors fail contrast. BUILD_PLAN **UX-047**.

## Acceptance criteria

- ✅ Settings Appearance includes “Use wallpaper colors” (`dynamic_color`)
- ✅ Preference persists in DataStore next to theme (`dynamic_color_enabled`, default on)
- ✅ `MultiAppShareTheme` applies dynamic color only when the toggle is on and SDK ≥ 31 (`DynamicColorPolicy`)

## Tests

- Automated: `DynamicColorPolicyTest`, `SettingsCatalogTest`, `SettingsDefaultsTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
