# Feature: duplicate-group-snackbar

> Duplicate group confirms the new name in a snackbar. BUILD_PLAN **UX-049**.

## Acceptance criteria

- ✅ Successful duplicate shows `snackbar_group_copied` with the unique copy name
- ✅ Name matches `GroupNameHelper.uniqueCopyName`
- ✅ Snackbar duration is Long (same as undo)

## Tests

- Automated: existing `GroupMutationsTest.duplicateGroup_copiesAppsWithUniqueName`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
