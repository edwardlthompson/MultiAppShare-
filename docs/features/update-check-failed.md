# Feature: update-check-failed

> A failed GitHub `releases/latest` check shows Later + Retry without blocking groups. BUILD_PLAN **UX-055**.

## Acceptance criteria

- ✅ Network/timeout/parse failures become `LaunchPrompt.CheckFailed`
- ✅ Dialog copy explains the miss; Retry re-runs `AppUpdates.evaluate`; Later waits 24h
- ✅ Empty assets / same version stay silent
- ✅ Groups list is not replaced by the check

## Tests

- Automated: `LaunchPromptTest.failedFetchOffersRetryAndEmptyAssetsStaySilent`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
