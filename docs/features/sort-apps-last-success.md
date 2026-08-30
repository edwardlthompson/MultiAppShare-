# Feature: sort-apps-last-success

> Order apps within a group by the most recent successful share completion. BUILD_PLAN **AE.24**.

## Acceptance criteria

- ✅ `AppSuccessSorter.sortByLastSuccess` places packages with higher completion timestamps first
- ✅ Untracked packages fall back deterministically to case-insensitive alphabetical sorting
- ✅ Fully local timestamp matching

## Tests

- Automated: `AppSuccessSorterTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
