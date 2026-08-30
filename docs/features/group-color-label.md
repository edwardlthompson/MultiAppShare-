# Feature: group-color-label

> Optional emoji or visual label prefix on a group name. BUILD_PLAN **AE.17**.

## Acceptance criteria

- ✅ `GroupColorLabel.formatWithEmoji` prefixes an emoji cleanly to a group title
- ✅ `GroupColorLabel.extractEmoji` detects known emoji badges on group names
- ✅ `GroupColorLabel.stripEmoji` retrieves the raw name for editing or searching

## Tests

- Automated: `GroupColorLabelTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
