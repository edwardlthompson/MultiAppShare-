# Feature: feature-spec-honesty

> Feature specs named in the UX review split domain vs user-visible and point at real composables. BUILD_PLAN **UX-078**.

## Acceptance criteria

- ✅ Review locks (`pin`, `notes`, `color-label`, `widget`, `MIME`, `settings`) are `Layer: user-visible`
- ✅ Each lock names a composable file that exists
- ✅ Domain helpers stay documented as domain, not fake UI

## Tests

- Automated: `FeatureSpecHonestyTest`, `FeatureSpecHonestyFilesTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
