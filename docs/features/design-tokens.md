# Feature: design-tokens

> Shared spacing, type, and color tokens replace Compose template purple and unused glass. BUILD_PLAN **UX-021**.

## Acceptance criteria

- `Dimens` uses a 4dp grid (space, dialog list heights, card elevation, share blur/scrim)
- `Type.kt` defines headline/title/body/label styles
- Fallback colors are teal, not `0xFF6650A4` template purple
- Unused `GlassySurface` is removed
- MainScreen share backdrop blur/scrim reads from `Dimens`

## Tests

- Automated: `ThemeTokensTest.primaryIsNotComposeTemplatePurple`; Paparazzi goldens re-recorded for the new scheme

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
