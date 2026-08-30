# Feature: privacy-policy-screen

> Scrollable in-app privacy screen outlining zero telemetry and local-only storage commitments. BUILD_PLAN **AE.34**.

## Acceptance criteria

- ✅ `PrivacyPolicyText` provides clear, readable privacy principles
- ✅ Enforces local-only storage and explicit user actions for clipboard access
- ✅ 100% offline-readable

## Tests

- Automated: `PrivacyPolicyTextTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
