# Feature: crash-review-snooze

> Snooze crash review prompts for the current app version (never auto-send). BUILD_PLAN **AE.70**.

## Acceptance criteria

- ✅ `CrashReviewSnoozeHelper` suppresses crash review prompt if already snoozed on the active version code
- ✅ Automatically re-evaluates when the app updates to a new version code
- ✅ Unit tests verify snooze evaluation and version comparison

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
