# Feature: autogroup-dry-run

> Pure preview calculation for automatic grouping buckets before saving. BUILD_PLAN **AE.22**.

## Acceptance criteria

- ✅ `AutoGroupDryRun.preview` computes auto-group buckets without database writes
- ✅ Handles overwrite and append modes with category inference
- ✅ Allows users and ViewModels to preview group distribution before persisting

## Tests

- Automated: `AutoGroupDryRunTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
