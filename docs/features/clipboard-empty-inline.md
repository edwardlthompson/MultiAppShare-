# Feature: clipboard-empty-inline

> Empty clipboard shows a lasting sentence on groups home, not a vanishing toast. BUILD_PLAN **UX-064**.

## Acceptance criteria

- ✅ Empty clipboard sets `ShareSessionState.noticeRes` (inline banner, not Toast)
- ✅ Copy tells the user to copy first, then tap Share clipboard again
- ✅ QS still opens the activity so the banner can appear

## Tests

- Automated: `ClipboardEmptyNoticeTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
