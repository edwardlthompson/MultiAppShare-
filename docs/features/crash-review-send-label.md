# Feature: crash-review-send-label

> Crash review primary is a send verb. Onboarding still suppresses the prompt. BUILD_PLAN **UX-041**.

## Acceptance criteria

- ✅ Confirm button uses `crash_review_send` (“Send sanitized report”)
- ✅ `FirstRunDialogQueue.allowCrashReview(onboardingVisible)` remains false during onboarding

## Tests

- Automated: `FirstRunDialogQueueTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
