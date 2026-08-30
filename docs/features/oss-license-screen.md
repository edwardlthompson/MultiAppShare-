# Feature: oss-license-screen

> In-app catalog and viewer for third-party open-source software licenses. BUILD_PLAN **AE.33**.

## Acceptance criteria

- ✅ `OpenSourceLicenseCatalog.LICENSES` provides static metadata for all third-party dependencies
- ✅ Confirms all bundled components use permissible FOSS licenses (Apache 2.0 / MIT)
- ✅ Zero telemetry SDKs or proprietary trackers

## Tests

- Automated: `OpenSourceLicensesTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
