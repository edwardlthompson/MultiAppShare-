# Feature: group-notes

> Optional short description or notes on a group. BUILD_PLAN **AE.18**.
>
> Layer: user-visible (`RenameGroupDialog`). Domain: `GroupNotesHelper`.

## Acceptance criteria

- ✅ User-visible: rename dialog notes field (`RenameGroupDialog`); list shows notes when present
- ✅ Domain: `GroupNotesHelper.sanitize` trims and bounds group notes to `MAX_NOTE_LENGTH` (140 chars)
- ✅ Domain: `GroupNotesHelper.hasNotes` accurately checks note existence

## Tests

- Automated: `GroupNotesHelperTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
