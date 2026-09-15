# Feature: about-offline-surfaces

> About opens privacy, licenses, and changelog on-device. BUILD_PLAN **UX-040**.

## Acceptance criteria

- ✅ Changelog row launches `WhatsNewDialog` (offline catalog), not GitHub in a browser
- ✅ Privacy and OSS licenses open existing in-app dialogs
- ✅ Telegram and donate still use `ExternalIntent` with a snackbar on failure

## Tests

- Automated: existing `WhatsNewHelperTest`; About wiring compiles under `./gradlew test`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
