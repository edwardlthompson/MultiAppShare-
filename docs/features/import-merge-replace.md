# Feature: import-merge-replace

> Choose between merging or replacing existing groups during backup import. BUILD_PLAN **AE.21**.

## Acceptance criteria

- ✅ `ImportStrategy` defines `REPLACE` and `MERGE` behavior
- ✅ `BackupImportResolver.resolve` supports non-destructive group merging with deduplicated app entries
- ✅ Preserves highest usage count and IDs during group merge

## Tests

- Automated: `BackupImportResolverTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
