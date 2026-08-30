# Feature: empty-groups-cta

> Autofill vs create when the group list is empty. BUILD_PLAN **AE.64**.

## Acceptance criteria

- ✅ `EmptyGroupsCtaHelper` evaluates when the empty list call-to-action should be displayed
- ✅ Action resolver maps choices for Autofill (smart categorisation) vs Manual creation
- ✅ Unit tests verify state evaluation and action labels

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
