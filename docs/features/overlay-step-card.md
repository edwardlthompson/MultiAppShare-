# Feature: overlay-step-card

> Sequential sharing shows the current app on an elevated card with sticky Continue. BUILD_PLAN **UX-074**.

## Acceptance criteria

- ✅ `SharingInProgressCopy` sits in an `ElevatedCard`
- ✅ Next/Finish stay sticky outside the card (`ShareOverlayActions.PRIMARY_STICKY`)
- ✅ Headline is still the current app (UX-054)

## Tests

- Automated: `OverlayStepCardTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
