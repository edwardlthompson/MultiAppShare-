# Feature: group-card-tap-expand

> Phone home group cards expand on tap. Overflow still owns edit actions. BUILD_PLAN **UX-046**.

## Acceptance criteria

- ✅ Compact home tap toggles expand (`GroupCardTap.Action.TOGGLE_EXPAND`)
- ✅ Two-pane tap still selects the group for the detail pane
- ✅ Share overlay tap still starts the sequential share
- ✅ Overflow ⋮ remains the action menu

## Tests

- Automated: `GroupCardTapTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
