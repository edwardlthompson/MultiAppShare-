# Feature: group-template-export

> Export a single group as a local, sanitized JSON template with zero secrets and reset usage stats. BUILD_PLAN **AE.20**.

## Acceptance criteria

- ✅ `GroupTemplateCodec.exportTemplate` encodes an isolated group structure to JSON, ensuring `id` is populated and `usageCount` is zeroed
- ✅ `GroupTemplateCodec.importTemplate` safely parses single-group templates and handles malformed input gracefully
- ✅ Local-only JSON encoding with no network or proprietary SDKs

## Tests

- Automated: `GroupTemplateCodecTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
