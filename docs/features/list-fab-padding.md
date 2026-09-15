# Feature: list-fab-padding

> Home group list keeps the last overflow menu above the single FAB. BUILD_PLAN **UX-043**.

## Acceptance criteria

- ✅ `GroupList` `LazyColumn` uses bottom `contentPadding` from `ListFabPadding`
- ✅ Home padding is at least 80dp (88dp after the single FAB in UX-005)
- ✅ Share overlay list uses 0dp extra padding (no home FAB)

## Tests

- Automated: `ListFabPaddingTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
