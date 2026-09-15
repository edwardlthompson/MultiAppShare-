# Feature: pin-unsupported-dialog

> Unsupported launchers get a dialog, not a flash toast. BUILD_PLAN **UX-050**.

## Acceptance criteria

- ✅ `PinShortcutPolicy.shouldExplainUnsupported` is true when pin is not supported
- ✅ `PinUnsupportedDialog` uses `pin_unsupported_title` plus `toast_pin_unsupported` body
- ✅ No Toast on the pin-failure path

## Tests

- Automated: `PinShortcutPolicyTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
