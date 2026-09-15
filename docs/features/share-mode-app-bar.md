# Feature: share-mode-app-bar

> Share mode keeps a Close top bar and “Choose a group” title. BUILD_PLAN **UX-032**.

## Acceptance criteria

- ✅ Share mode shows `ShareModeTopBar` with Close (`onCancelShare`) and `share_overlay_title`
- ✅ Search remains available in share mode (UX-026)
- ✅ Instruction card is subtitle-only so the title is not duplicated
- ✅ Selected groups use `primaryContainer`; other share-mode rows use `surfaceContainerHigh`

## Tests

- Automated: existing Paparazzi group list / filter goldens still verify

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
