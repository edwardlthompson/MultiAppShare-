# Feature: silent-external-launch

> URL and settings intents show a snackbar when no handler exists. BUILD_PLAN **UX-034**.

## Acceptance criteria

- ✅ `ExternalIntent.tryViewUrl` / `tryStart` return false on `ActivityNotFoundException` instead of swallowing silently
- ✅ Donate, update, changelog, Telegram, OSS license links, feedback GitHub, and battery settings notify with `error_no_app_for_link`
- ✅ Feedback stays open and shows the same error in-dialog when GitHub cannot launch

## Tests

- Automated: `ExternalIntentTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
