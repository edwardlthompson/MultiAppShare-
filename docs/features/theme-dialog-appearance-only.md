# Feature: theme-dialog-appearance-only

> Theme dialog is appearance only. Crash capture, high refresh, and haptics live in Settings sections. BUILD_PLAN **UX-009**.

## Acceptance criteria

- ✅ `ThemeDialog` has system / light / dark only
- ✅ Haptics, high refresh, and crash-save are Settings catalog rows with switches
- ✅ Theme dialog TalkBack radios unchanged; switch rows use `Modifier.selectable` + `onCheckedChange = null`

## Tests

- Automated: `SettingsCatalogTest` (appearance includes refresh), `SettingsInvokeTest` (haptics does not open theme)

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
