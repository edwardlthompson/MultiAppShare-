# Feature: share-payload-sheet

> One bottom sheet shows MIME, text snippet, URI thumbnails, and reorder before the first sequential handoff. BUILD_PLAN **UX-016**.

## Acceptance criteria

- Tapping a group in share mode opens one sheet (not stacked preview then reorder dialogs)
- Image/video (and unknown `*/*`) URIs render Coil thumbnails; text snippet is visible
- Multiple URIs can be reordered with Move up / Move down on the same sheet
- Continue applies order and starts sharing; Cancel does not launch a target app
- `preview_thumbnail_cd` exists in en / es / fr / de / it

## Tests

- Automated: `PayloadPreviewTest.showsThumbnail_forImageAndVideoMime`, existing `PayloadReorder` tests, Paparazzi of sheet content with a text snippet

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
