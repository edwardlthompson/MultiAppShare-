# Feature: share-overlay-actions

> One primary action on the sequential overlay. BUILD_PLAN **UX-010**.

## Acceptance criteria

- ✅ Primary filled button is Next or Finish
- ✅ Replay and Previous are outlined
- ✅ Skip this app, Skip remaining, and Pause live in More
- ✅ Try later only when `ShareDefer.shouldOffer`
- ✅ Hero shows current app icon and label; paused state is labeled

## Tests

- Automated: `ShareOverlayActionsTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
