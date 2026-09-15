# Feature: leftover-dialogs-gone

> Production About/History use Dashboard full-screen destinations. Leftover `AboutDialog` is removed; `HistoryDialog` and `GlassySurface` were already gone. BUILD_PLAN **UX-036**.

## Acceptance criteria

- ✅ `MainActivityDialogs.kt` keeps empty-groups + delete confirm only
- ✅ No `AboutDialog` or `HistoryDialog` composable in app main
- ✅ `GlassySurface.kt` is absent

## Tests

- Automated: `LeftoverDialogsGoneTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
