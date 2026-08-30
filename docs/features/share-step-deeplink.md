# Feature: share-step-deeplink

> Notification tap / deeplink resumes the exact sequential share step. BUILD_PLAN **AE.11**.

## Acceptance criteria

- ✅ `ShareDeeplinkBuilder.buildUri` generates `multiappshare://share_step?index=N`
- ✅ `ShareDeeplinkBuilder.parseStepIndex` extracts target step index
- ✅ Rejects invalid/malformed URI schemes gracefully
- ✅ Local/offline handling

## Tests

- Automated: `ShareDeeplinkBuilderTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
