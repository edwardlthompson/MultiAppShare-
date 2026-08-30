# Feature: notification-channel-link

> Deep link intent data builder for system notification and notification channel settings. BUILD_PLAN **AE.43**.

## Acceptance criteria

- ✅ `NotificationChannelLink.buildChannelSettingsIntentData` creates intent parameter bundles for direct channel management
- ✅ Supports top-level app notification settings when channel ID is unspecified
- ✅ Matches standard Android platform extras (`EXTRA_APP_PACKAGE`, `EXTRA_CHANNEL_ID`)

## Tests

- Automated: `NotificationChannelLinkTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
