# Feature: notification-permission-in-context

> Ask for `POST_NOTIFICATIONS` when a share session starts, not on cold launch. BUILD_PLAN **UX-001**.

## Acceptance criteria

- ✅ Cold start does not request notification permission
- ✅ Entering a share session on API 33+ requests permission if it is not granted
- ✅ Denied state during share shows a banner with a settings CTA via `NotificationChannelLink`
- ✅ Share continues even if the user denies

## Tests

- Automated: `NotificationPermissionPolicyTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
