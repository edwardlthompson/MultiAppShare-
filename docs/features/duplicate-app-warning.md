# Feature: duplicate-app-warning

> Warn when the same app package is assigned across multiple groups. BUILD_PLAN **AE.16**.

## Acceptance criteria

- ✅ `DuplicateAppDetector.findOtherGroupsContaining` identifies other groups that contain a given package
- ✅ `DuplicateAppDetector.findDuplicatePackages` maps duplicate packages to their host group names
- ✅ Prevents accidental redundant sharing across distinct groups

## Tests

- Automated: `DuplicateAppDetectorTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
