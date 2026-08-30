# Feature: overflow-feedback

> Feedback entry on the overflow menu linking directly to GitHub Issues prefilled with device info. BUILD_PLAN **AE.37**.

## Acceptance criteria

- ✅ `OverflowFeedbackHelper.formatFeedbackUrl` creates encoded issue submission URLs
- ✅ Includes app version and Android API level without personal identifiers
- ✅ Zero proprietary analytics or third-party feedback SDKs

## Tests

- Automated: `OverflowFeedbackHelperTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
