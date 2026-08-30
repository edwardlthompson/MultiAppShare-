# Feature: reduce-motion-burst

> Skip the success Canvas burst when animator or transition scale is off. BUILD_PLAN **AE.7**.

## Acceptance criteria

- ✅ Animator duration scale `0` or transition scale `0` skips the burst and finishes immediately
- ✅ Default scales (`1`) keep the existing animation
- ✅ Offline: reads system settings locally
- ✅ Accessibility: honors Android “Remove animations”

## Tests

- Automated: `ReduceMotionTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
