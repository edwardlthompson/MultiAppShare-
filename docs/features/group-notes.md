# Feature: group-notes

> Optional short description or notes on a group. BUILD_PLAN **AE.18**.

## Acceptance criteria

- ✅ `GroupNotesHelper.sanitize` trims and bounds group notes to `MAX_NOTE_LENGTH` (140 chars)
- ✅ `GroupNotesHelper.hasNotes` accurately checks note existence
- ✅ Pure Kotlin domain model support for optional group annotations

## Tests

- Automated: `GroupNotesHelperTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
