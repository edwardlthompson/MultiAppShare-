# Feature: mime-mismatch-sheet

> Explain why a group is hidden or incompatible for the current share MIME type. BUILD_PLAN **AE.10**.

## Acceptance criteria

- ✅ `MimeMismatchExplanation.formatExplanation` provides user-readable reason for incompatibility
- ✅ `MimeMismatchExplanation.isGroupCompatible` accurately determines if any app in a group supports the MIME type
- ✅ Handles empty / fallback MIME types gracefully
- ✅ Local/offline calculation only

## Tests

- Automated: `MimeMismatchExplanationTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
