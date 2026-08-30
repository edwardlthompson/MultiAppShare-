# Feature: fgs-timeout

> Survive Android 15 foreground-service 6-minute timeout by gracefully demoting or terminating background sharing tasks. BUILD_PLAN **AE.42**.

## Acceptance criteria

- ✅ `FgsTimeoutGuard.shouldDemoteOrStop` identifies foreground services nearing or exceeding the 6-minute Android 15 timeout limit
- ✅ Prevents ForegroundServiceTimeoutException crashes
- ✅ Keeps data sharing clean and resilient

## Tests

- Automated: `FgsTimeoutGuardTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
