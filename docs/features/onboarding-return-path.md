# Feature: onboarding-return-path

> Teach Recents + notification + Next App on first run. BUILD_PLAN **UX-003**.

## Acceptance criteria

- ✅ Onboarding page 2 explains leaving the app, returning via Recents or the share notification, then Next App
- ✅ Autofill remains the primary action on that page
- ✅ en / es / fr copy

## Tests

- Automated: existing `OnboardingDialogPaparazziTest` still snapshots page 1 (Skip). Copy is string resources covered by locale files.

## Fallback validation

- Command: `./gradlew :app:lintDebug`
