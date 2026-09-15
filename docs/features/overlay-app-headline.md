# Feature: overlay-app-headline

> Share overlay titles the current app; content and step sit in the subtitle. BUILD_PLAN **UX-054**.

## Acceptance criteria

- ✅ Headline is the current app label (`SharingHeadline.title`); content noun is fallback only
- ✅ Subtitle is content + step (`sharing_overlay_subtitle`)
- ✅ Duplicate “Next: {app}” line is gone (`SharingHeadline.SHOW_NEXT_APP_LINE`)

## Tests

- Automated: `SharingHeadlineTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
