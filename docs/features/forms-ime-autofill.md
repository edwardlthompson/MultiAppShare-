# Feature: forms-ime-autofill

> Form fields expose IME actions, password autofill types, and described-by errors. BUILD_PLAN **UX-042**.

## Acceptance criteria

- ✅ Backup passphrase uses `KeyboardType.Password` plus Compose `ContentType` (`NewPassword` on export/confirm, `Password` on import)
- ✅ Group name, notes, feedback, and search fields set `KeyboardOptions` (Done / Next / Search)
- ✅ Create/Rename keep `supportingText` errors on the name field
- ✅ Modify-apps rows are at least 48dp and the checkbox toggles selection

## Tests

- Automated: `FormFieldPolicyTest`, `GroupEditorSearchTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
