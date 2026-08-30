# Feature: collapse-unused-overlay-groups

> Collapse group accordions that have zero MIME-compatible apps in the current share sheet overlay. BUILD_PLAN **AE.25**.

## Acceptance criteria

- ✅ `OverlayGroupCollapser.resolveExpansionStates` automatically collapses incompatible groups when enabled
- ✅ Groups with compatible apps remain expanded
- ✅ Supports disabling auto-collapse

## Tests

- Automated: `OverlayGroupCollapserTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
