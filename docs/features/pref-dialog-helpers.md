# Feature: pref-dialog-helpers

> Delay and language dialogs include one-line helper copy. BUILD_PLAN **UX-051**.

## Acceptance criteria

- ✅ Sharing delay dialog explains the wait happens after you return
- ✅ Language dialog explains only this app’s labels change
- ✅ Helper uses `bodySmall` / onSurfaceVariant via `SettingsDialogHelper`

## Tests

- Automated: string resources compile under `./gradlew test` (dialog is static copy)

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
