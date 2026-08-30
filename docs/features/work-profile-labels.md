# Feature: work-profile-labels

> Disambiguate work vs personal share targets. BUILD_PLAN **AE.66**.

## Acceptance criteria

- ✅ `WorkProfileDisambiguation` formats target app names by distinguishing work profile apps with a clear suffix
- ✅ Supports user handle / user ID evaluation for multi-profile systems
- ✅ Unit tests verify formatting for both personal and work profiles

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
