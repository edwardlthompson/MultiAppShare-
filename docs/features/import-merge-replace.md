# Feature: import-merge-replace

> Choose between merging or replacing existing groups during backup import. BUILD_PLAN **AE.21**.

## Acceptance criteria

- ✅ `ImportStrategy` defines `REPLACE` and `MERGE` behavior
- ✅ `BackupImportResolver.resolve` supports non-destructive group merging with deduplicated app entries
- ✅ Preserves highest usage count and IDs during group merge
- ✅ Import picker offers Merge as the safe action and Replace in error color (UX-039)
- ✅ Replace with existing groups shows a localized overwrite warning (`plurals/import_replace_warning`)

## Tests

- Automated: `BackupImportResolverTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
