# Feature: notification-denial-recovery

> Denied notification permission shows an in-session banner with a settings CTA. BUILD_PLAN **UX-065**.

## Acceptance criteria

- ✅ `NotificationPermissionPolicy.shouldShowSettingsBanner` is true only during share when denied on API 33+
- ✅ Banner opens `NotificationPermissionIntents.appSettings` via `NotificationChannelLink`
- ✅ Cold start still does not prompt (`PROMPT_ON_COLD_START` is false)

## Tests

- Automated: `NotificationPermissionPolicyTest`, `NotificationPermissionIntentsTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
