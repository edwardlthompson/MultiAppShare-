# Feature: history-filter-group

> Filter local share history items by group name. BUILD_PLAN **AE.28**.

## Acceptance criteria

- ✅ `HistoryGroupFilter.filter` filters history items case-insensitively by group name
- ✅ Empty/null group filter returns all history entries
- ✅ `HistoryGroupFilter.extractDistinctGroups` provides the list of unique groups represented in history

## Tests

- Automated: `HistoryGroupFilterTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
