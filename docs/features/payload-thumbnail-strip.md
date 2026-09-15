# Feature: payload-thumbnail-strip

> Share payload sheet shows URI thumbnails and a text snippet before the first handoff. BUILD_PLAN **UX-071**.

## Acceptance criteria

- ✅ `SharePayloadSheet` uses `AsyncImage` when `PayloadPreview.showsThumbnail`
- ✅ Text snippet still appears for `text/plain`
- ✅ Continue starts the sequence; dismiss does not

## Tests

- Automated: `PayloadPreviewTest.showsThumbnail_forImageAndVideoMime`, `SharePayloadSheetPaparazziTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
