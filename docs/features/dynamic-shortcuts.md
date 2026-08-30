# Feature: dynamic-shortcuts

> Rank dynamic launcher shortcuts by group usage stats. BUILD_PLAN **AE.44**.

## Acceptance criteria

- ✅ `DynamicShortcutBuilder.buildTopShortcuts` ranks up to `MAX_SHORTCUTS` (4) groups by usage count
- ✅ Truncates short labels appropriately for launcher display limits
- ✅ Computes strictly from local usage statistics

## Tests

- Automated: `DynamicShortcutBuilderTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
