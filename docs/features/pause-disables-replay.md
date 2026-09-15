# Feature: pause-disables-replay

> While a share session is paused, Replay/Retry cannot launch the current app. BUILD_PLAN **UX-038**.

## Acceptance criteria

- ✅ Replay/Retry button is `enabled = !paused`
- ✅ `SharePause.replayAllowed` is false when paused
- ✅ `replayShareStep` no-ops while paused

## Tests

- Automated: `SharePauseTest.blocksReplayWhilePaused`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
