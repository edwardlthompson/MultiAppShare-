# Feature: merge-preview-count

> Merge uses the Merge verb and shows how many apps the target will contain. BUILD_PLAN **UX-060**.

## Acceptance criteria

- ✅ Confirm label is `button_merge`
- ✅ Selected target shows `merge_preview_apps` from `MergePreview.combinedAppCount`

## Tests

- Automated: `MergePreviewTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
