# Feature: autogroup-preview-sheet

> Auto-group shows a preview of proposed buckets before writing. BUILD_PLAN **UX-052**.

## Acceptance criteria

- ✅ Overflow Auto Group uses append preview; empty/onboarding uses replace preview
- ✅ `AutoGroupDryRun.preview` + `summarize` drive the dialog rows
- ✅ Confirm calls `autoGroupApps`; onboarding confirm also dismisses onboarding
- ✅ Empty preview disables confirm

## Tests

- Automated: `AutoGroupDryRunTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
