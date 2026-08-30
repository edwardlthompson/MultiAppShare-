# Feature: haptics-toggle

> Settings toggle for sequential share finish haptics. BUILD_PLAN **AE.8**. Default on. Not included in backup.

## Acceptance criteria

- ✅ Theme/settings dialog offers share haptics checkbox
- ✅ Finish haptic runs only when the preference is on
- ✅ Default is on (same as current behavior)
- ✅ i18n: `settings_share_haptics`

## Tests

- Automated: `ShareHapticsTest` + `SettingsDefaultsTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
