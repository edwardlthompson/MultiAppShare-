# Feature: mime-mismatch-sheet

> Explain why a group is hidden or incompatible for the current share MIME type. BUILD_PLAN **AE.10**.
>
> Layer: user-visible (`CompatibleGroupsEmptyState` in `MainScreenShareHelpers`). Domain: `MimeMismatchExplanation`.

## Acceptance criteria

- ✅ User-visible: empty share-mode state explains the MIME kind and offers Add group (`MainScreenShareHelpers`)
- ✅ Domain: `MimeMismatchExplanation.formatExplanation` provides a user-readable reason
- ✅ Domain: `MimeMismatchExplanation.isGroupCompatible` checks whether any app in a group supports the MIME type
- ✅ Handles empty / fallback MIME types gracefully
- ✅ Local/offline calculation only
- ✅ `mimeKind` maps MIME to image/video/text/other for UI nouns
- ✅ Empty share-mode state has Add group CTA (UX-013)

## Tests

- Automated: `MimeMismatchExplanationTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
