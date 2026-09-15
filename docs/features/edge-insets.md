# Feature: edge-insets

> Edge-to-edge content clears the navigation bar, gesture inset, and IME. BUILD_PLAN **UX-020**.

## Acceptance criteria

- Add-group FAB uses `navigationBarsPadding`
- Sequential share actions sit above the nav bar and IME (`navigationBarsPadding` + `imePadding`)
- Create, rename, and backup-import fields use `imePadding`

## Tests

- Automated: not feasible for system window insets on the host (no emulator in default CI). Fallback: `bash scripts/feature-gate.sh --stack android`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
