# Feature: in-app-changelog

> Offline-accessible, cached changelog highlights. BUILD_PLAN **AE.38**.

## Acceptance criteria

- ✅ `OfflineChangelogCatalog.ENTRIES` provides bundled release highlights
- ✅ `OfflineChangelogCatalog.getLatestVersion` surfaces the current latest version string
- ✅ Readable without network access

## Tests

- Automated: `OfflineChangelogCatalogTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
