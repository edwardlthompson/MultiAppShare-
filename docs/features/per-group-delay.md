# Feature: per-group-delay

> Optional share delay override per group. BUILD_PLAN **AE.9**.

## Acceptance criteria

- ✅ `GroupDelayOverride.resolveDelayMs` resolves delay between global and group override
- ✅ When per-group override is null/unset, falls back to global delay
- ✅ Clamps delays to valid range (0 - 5000ms)
- ✅ Offline: local calculation only

## Tests

- Automated: `GroupDelayOverrideTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
