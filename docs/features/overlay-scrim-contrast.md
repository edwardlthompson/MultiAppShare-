# Feature: overlay-scrim-contrast

> Share overlay scrim stays in Material 32–60% even on low-RAM (no blur). Copy sits on opaque cards with onPrimaryContainer. BUILD_PLAN **UX-076**.

## Acceptance criteria

- ✅ `ShareBackdropScrim.alpha` is within 0.32–0.60 for low-RAM and default
- ✅ Low-RAM is heavier than default but not 0.78
- ✅ Overlay header uses `onPrimaryContainer` at full opacity

## Tests

- Automated: `ShareBackdropScrimTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
