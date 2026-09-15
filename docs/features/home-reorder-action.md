# Feature: home-reorder-action

> Group share order is reachable from the home top bar. BUILD_PLAN **UX-053**.

## Acceptance criteria

- ✅ Top bar shows a Reorder action when 2+ groups exist (`HomeTopBarActions.showReorder`)
- ✅ The action opens the existing `SortGroupsDialog` (up/down)
- ✅ Overflow **Sort Groups** remains as a fallback
- ✅ Empty / single-group home hides the top-bar control

## Tests

- Automated: `HomeTopBarActionsTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
