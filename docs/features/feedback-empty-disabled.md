# Feature: feedback-empty-disabled

> Feedback Send stays disabled until title, body, or an attached crash is present. BUILD_PLAN **UX-058**.

## Acceptance criteria

- ✅ `FeedbackPreview.canSend` is false when title and body are blank and there is no crash text
- ✅ Confirm button uses `enabled = canSend`
- ✅ Crash-attached feedback can still send

## Tests

- Automated: `FeedbackPreviewTest.sendDisabledWhenTitleAndBodyAreBlank`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
