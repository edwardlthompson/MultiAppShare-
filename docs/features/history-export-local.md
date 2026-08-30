# Feature: history-export-local

> Sanitized local JSON export of share history without payload data or secret text. BUILD_PLAN **AE.29**.

## Acceptance criteria

- ✅ `LocalHistoryExporter.exportToJson` strips payloads and sensitive content descriptions from exported records
- ✅ Outputs structured, clean JSON document with export timestamp and status metadata
- ✅ 100% offline and privacy-first

## Tests

- Automated: `LocalHistoryExporterTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
