# Feature: in-app-legal-screens

> Privacy and OSS licenses are scrollable in-app destinations from About and Settings. BUILD_PLAN **UX-062**.

## Acceptance criteria

- ✅ `PrivacyPolicyDialog` renders `PrivacyPolicyText.SECTIONS`
- ✅ `OssLicensesDialog` renders `OpenSourceLicenseCatalog.LICENSES`
- ✅ Settings catalog includes `privacy` and `licenses`

## Tests

- Automated: `AboutLegalSurfacesTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
