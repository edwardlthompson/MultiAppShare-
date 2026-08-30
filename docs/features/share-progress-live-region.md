# Feature: share-progress-live-region

> TalkBack announces step N of M and the current target when the overlay advances. BUILD_PLAN **AE.6**.

## Acceptance criteria

- ✅ Sequential overlay exposes a polite live region with step and target
- ✅ Out-of-range index produces no snapshot (no empty announcement)
- ✅ Offline: local only
- ✅ i18n: `sharing_progress_announce`

## Tests

- Automated: `ShareProgressAnnounceTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
