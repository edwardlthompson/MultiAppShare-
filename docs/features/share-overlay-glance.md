# Feature: share-overlay-glance

> Sequential overlay shows progress, target identity, and remaining count at a glance. BUILD_PLAN **UX-012**.

## Acceptance criteria

- ✅ `LinearProgressIndicator` bound to `ShareOverlayActions.progressFraction`
- ✅ Current app icon via Coil `AppInfo` + label
- ✅ Existing live region `sharing_progress_announce` unchanged

## Tests

- Automated: `ShareOverlayActionsTest.progressFractionMatchesStep`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
