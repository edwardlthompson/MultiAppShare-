# Feature: ghost-features

> Domain helpers that had no user-visible control are wired into the app. BUILD_PLAN **UX-015**.

## Acceptance criteria

- Pin/unpin on the group overflow menu; list order uses `GroupPinOrder`
- Rename dialog includes emoji presets (`GroupColorLabel`) and a notes field (`GroupNotesHelper`, Room `notes`)
- Import asks Merge vs Replace (`BackupImportResolver` / `ImportConfirmationHelper`)
- Settings includes battery, privacy, licenses, and what’s new
- Backup export passphrase shows `PassphraseStrengthMeter`
- Share overlay width uses `DesktopWindowingHelper.overlayMaxContentWidthDp`
- App rows show `(Work)` via `WorkProfileDisambiguation`
- Home screen widget uses `HomeWidgetModel`
- Notification channel settings remain on the in-share banner

## Tests

- Automated: `SettingsCatalogTest.includesGhostFeatureSettingsRows`, `SettingsInvokeTest`, `GroupMutationsTest.renameGroup_sanitizesNotes`, `AppDatabaseMigrationsTest.migration2to3_addsGroupNotesColumn`, `WorkProfileDisambiguationTest.userIdFromUid_usesAndroidPerUserRange`, `LocaleParityTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
