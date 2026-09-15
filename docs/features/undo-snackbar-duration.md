# Feature: undo-snackbar-duration

> Delete-group undo stays on screen long enough to hit. BUILD_PLAN **UX-037**.

## Acceptance criteria

- ✅ `GroupDeleteUndoEffect` uses `SnackbarDuration.Long` (not Short)
- ✅ Scaffold snackbar host remains the only undo surface (clear of the single FAB)

## Tests

- Automated: existing `ShareUxSmokeInstrumentedTest` taps Undo; duration is compile-time `Long`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
