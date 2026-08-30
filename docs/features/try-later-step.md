# Feature: try-later-step

> Defer a failed sequential share target to the end of the queue without counting Skip. BUILD_PLAN **AE.5**.

## Acceptance criteria

- ✅ User-visible: after a failed handoff, **Try later** appears when at least one target remains
- ✅ Try later moves the current package to the end and opens the next target
- ✅ History does not record Skip for this action
- ✅ Hidden when paused, when the current target is last, or when the last handoff did not fail
- ✅ i18n: `sharing_try_later`

## Tests

- Automated: `ShareDeferTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
