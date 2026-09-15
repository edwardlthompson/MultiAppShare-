# Feature: screen-headings

> Each mode has one heading semantic on the top bar; overlay copy is not a second h1. BUILD_PLAN **UX-077**.

## Acceptance criteria

- ✅ Home top bar `Groups` uses `heading()`
- ✅ Share-mode top bar title uses `heading()` (choose-group or session chrome)
- ✅ Overlay copy is not a document heading (`ScreenHeadings.OVERLAY_COPY_IS_DOCUMENT_HEADING` is false)

## Tests

- Automated: `ScreenHeadingsTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
