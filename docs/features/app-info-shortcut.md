# Feature: app-info-shortcut

> Open system App Info settings directly from a group row. BUILD_PLAN **AE.68**.

## Acceptance criteria

- ✅ `AppInfoShortcutHelper` builds intent action and URI data target for Android system application details
- ✅ Validates package name patterns before dispatching
- ✅ Unit tests verify URI formatting and package name validation

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
