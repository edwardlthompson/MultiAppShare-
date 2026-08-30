# Feature: settings-search

> Filter and search rows in the Settings screen catalog. BUILD_PLAN **AE.39**.

## Acceptance criteria

- ✅ `SettingsCatalog.search` matches query against setting title, ID, or section name
- ✅ Blank/null search returns the entire catalog
- ✅ Local-only in-memory search

## Tests

- Automated: `SettingsCatalogTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
