# Feature: repro-verify-prerelease

> Wire reproducible unsigned APK hash verification into pre-release workflow. BUILD_PLAN **AE.61**.

## Acceptance criteria

- ✅ `scripts/verify-reproducible-apk.sh` and `verify-reproducible-apk.ps1` run deterministic APK build validation
- ✅ Verifies `SOURCE_DATE_EPOCH` reproducibility for F-Droid and release packaging
- ✅ Prevents non-reproducible release regressions

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
