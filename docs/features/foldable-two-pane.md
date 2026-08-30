# Feature: foldable-two-pane

> Foldable hinge awareness and posture-responsive two-pane layout calculations. BUILD_PLAN **AE.46**.

## Acceptance criteria

- ✅ `FoldableLayoutHelper.shouldSplitTwoPane` enables dual-pane split on screens ≥ 600dp width or when half-opened along a separating physical hinge
- ✅ Preserves single pane layout on flat compact devices
- ✅ Pure calculation decoupled from Android runtime dependencies

## Tests

- Automated: `FoldableLayoutHelperTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
