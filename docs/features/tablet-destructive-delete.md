# Feature: tablet-destructive-delete

> Tablet group pane Delete uses error color, not a save-adjacent outline. Confirm dialog stays. BUILD_PLAN **UX-079**.

## Acceptance criteria

- ✅ `GroupDetailPane` Delete is a `TextButton` with error content color
- ✅ Phone overflow Delete already uses error color
- ✅ Confirm dialog is unchanged (`DeleteGroupDialog`)

## Tests

- Automated: `DestructiveActionsTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
