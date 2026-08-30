# Feature: desktop-windowing

> Window size class classification and max content width limits for desktop freeform windows. BUILD_PLAN **AE.47**.

## Acceptance criteria

- ✅ `DesktopWindowingHelper.classifyWindowSize` classifies Compact (<600dp), Medium (600-839dp), and Expanded (≥840dp) widths
- ✅ `DesktopWindowingHelper.overlayMaxContentWidthDp` constrains overlay width on desktop/tablet displays
- ✅ Responsive across freeform window resizing

## Tests

- Automated: `DesktopWindowingHelperTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
