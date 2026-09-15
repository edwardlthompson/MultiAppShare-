# Feature: passphrase-ux

> Strength meter, show/hide, and confirm mismatch on backup passphrase dialogs. BUILD_PLAN **UX-023**.

## Acceptance criteria

- ✅ Export dialog shows `PassphraseStrengthMeter` progress and labeled tier before confirm
- ✅ Weak passphrases use the error color so the warning is not color-only-progress
- ✅ Passphrase and confirm fields have a visibility toggle (TalkBack show/hide)
- ✅ Confirm mismatch shows `isError` plus `passphrase_mismatch` helper; export stays disabled
- ✅ Import dialog also has a visibility toggle; existing field error stays inline
- ✅ en / es / fr / de / it include `passphrase_show`, `passphrase_hide`, `passphrase_mismatch`

## Tests

- Automated: `PassphraseFormTest`, Paparazzi `BackupPassphrasePaparazziTest`

## Fallback validation

- Command: `./gradlew :app:testDebugUnitTest --tests com.multiappshare.BackupPassphrasePaparazziTest`
