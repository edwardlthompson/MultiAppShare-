# Feature: copy-group-names

> Copy group app list to clipboard. BUILD_PLAN **AE.67**.

## Acceptance criteria

- ✅ `GroupNamesClipboardFormatter` formats single or multiple group app names into human-readable plain text
- ✅ Supports both individual group rows and bulk export of all groups to clipboard
- ✅ Unit tests verify formatting for empty groups, multi-app groups, and bulk formatting

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
