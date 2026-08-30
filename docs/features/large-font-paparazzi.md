# Feature: large-font-paparazzi

> Large font (200% accessibility scale) layout accommodation and overflow bounds. BUILD_PLAN **AE.49**.

## Acceptance criteria

- ✅ `FontScaleLayoutHelper.isLargeFont` flags font scales ≥ 1.5x
- ✅ `FontScaleLayoutHelper.resolveDialogMaxHeightFraction` expands height budget for dialogs on large accessibility fonts
- ✅ Prevents text clipping and overflow across dialog components

## Tests

- Automated: `FontScaleLayoutHelperTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
