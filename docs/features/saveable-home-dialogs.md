# Feature: saveable-home-dialogs

> Group filter and boolean dialog flags use `rememberSaveable`. BUILD_PLAN **UX-069**.

## Acceptance criteria

- ✅ `groupFilterQuery` is `rememberSaveable`
- ✅ Create/sort/history/about/settings/import boolean flags are `rememberSaveable`
- ✅ Share session stays on its own persist path

## Tests

- Automated: `HomeSaveableStateTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
