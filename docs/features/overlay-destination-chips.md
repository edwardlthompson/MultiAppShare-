# Feature: overlay-destination-chips

> Share overlay group cards show the sequence of app icons before start. BUILD_PLAN **UX-072**.

## Acceptance criteria

- ✅ `OverlayDestinationChips.shouldShow` is true in share mode with apps
- ✅ `OverlayAppChips` renders up to 6 icons on the overlay card
- ✅ Home list still uses expand, not chips

## Tests

- Automated: `OverlayDestinationChipsTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
