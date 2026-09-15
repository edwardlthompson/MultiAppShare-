# Feature: first-run-dialog-queue

> At most one blocking in-app dialog on first Success. BUILD_PLAN **UX-004**.

## Acceptance criteria

- ✅ Onboarding suppresses crash review and donate/update
- ✅ Active share suppresses donate/update (already skipped `AppUpdatesHost` in share mode)
- ✅ Crash review suppresses donate/update until dismissed
- ✅ POST_NOTIFICATIONS is not a cold-start stack (UX-001)

## Tests

- Automated: `FirstRunDialogQueueTest`

## Fallback validation

- Command: `./gradlew :core-domain:test`
