# Feature: empty-state-glyph

> Empty lists show a glyph, a why, and one primary CTA. BUILD_PLAN **UX-070**.

## Acceptance criteria

- ✅ Empty groups: Apps glyph; Create is the filled button; Auto-group is text
- ✅ Filter miss, incompatible share, and empty history each show a glyph
- ✅ Paparazzi empty-groups goldens updated

## Tests

- Automated: `EmptyGroupsPlaceholderPaparazziTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
