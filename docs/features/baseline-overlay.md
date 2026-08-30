# Feature: baseline-overlay

> Baseline profile verification for share target cold startup and overlay animation rendering. BUILD_PLAN **AE.57**.

## Acceptance criteria

- ✅ `app/src/main/generated/baselineProfiles/baseline-prof.txt` contains AOT precompilation rules for core Compose and sharing workflows
- ✅ Guarantees smooth 60/120fps entry for share target overlay activities
- ✅ Verified build packaging

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
