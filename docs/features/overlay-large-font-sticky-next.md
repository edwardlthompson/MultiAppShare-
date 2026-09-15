# Feature: overlay-large-font-sticky-next

> Share overlay keeps Next/Finish pinned while copy scrolls at 200% font. BUILD_PLAN **UX-048**.

## Acceptance criteria

- ✅ Status copy scrolls; Replay/Previous/Next stay outside the scroll (`ShareOverlayActions.PRIMARY_STICKY`)
- ✅ Try later lives in More, not a fifth full-width button
- ✅ Dual FAB already removed (UX-005); overlay More has a single Finish early item

## Tests

- Automated: `ShareOverlayActionsTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
