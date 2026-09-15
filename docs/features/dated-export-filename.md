# Feature: dated-export-filename

> Group export filenames include a local date so Downloads copies do not collide. BUILD_PLAN **UX-059**.

## Acceptance criteria

- ✅ `BackupExportName.json` is `multiappshare-groups-YYYY-MM-DD.json`
- ✅ `CreateDocument` uses that name

## Tests

- Automated: `BackupExportNameTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
