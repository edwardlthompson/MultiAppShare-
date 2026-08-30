# Feature: whats-new-local

> Offline what's-new dialog after version update. BUILD_PLAN **AE.65**.

## Acceptance criteria

- ✅ `WhatsNewHelper` determines if what's-new dialog should be displayed following a version code change
- ✅ Reads highlights from `OfflineChangelogCatalog` with safe fallback
- ✅ Unit tests cover upgrade detection and highlights retrieval

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
