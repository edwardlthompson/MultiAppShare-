# Feature: tablet-desktop-pane

> Tablet detail pane shows the group’s apps, one primary Edit, and destructive Delete. Expanded windows use `DesktopWindowingHelper` max content width. BUILD_PLAN **UX-033**.

## Acceptance criteria

- ✅ Two-pane layout when window class is Medium or Expanded and not in share mode
- ✅ Detail pane lists apps (or empty hint) plus one primary Modify and error-colored Delete
- ✅ Compact and share-mode stay single-pane
- ✅ Content width is capped with `overlayMaxContentWidthDp` for the current size class

## Tests

- Automated: `GroupDetailPanePaparazziTest`, existing `DesktopWindowingHelperTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
