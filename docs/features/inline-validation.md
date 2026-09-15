# Feature: inline-validation

> Field errors stay on the form instead of Toasts. BUILD_PLAN **UX-017**.

## Acceptance criteria

- Duplicate group name on create/rename shows `isError` + `supportingText`; the typed name is kept
- Merge failure shows inline error on the merge dialog
- Wrong backup passphrase and import failures show on the passphrase field or a home banner; passphrase text is not cleared on failure
- Empty clipboard shows `InlineNoticeBanner` (not a Toast)
- History re-share failure shows error text on the history dialog

## Tests

- Automated: `InlineFieldErrorTest`, Paparazzi `ShareOverlayAccessibilityTest.createGroupDialog_light` with duplicate-name error

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
