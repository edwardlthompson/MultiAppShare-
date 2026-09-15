# Feature: create-rename-submit

> Disable Create/Save until the group name is non-blank; IME Done submits. BUILD_PLAN **UX-024**.

## Acceptance criteria

- ✅ Create and Rename primaries use `enabled = GroupNameHelper.canSubmit(name)`
- ✅ Blank or whitespace-only names cannot submit
- ✅ IME Done on the name field submits only when valid
- ✅ Duplicate-name inline errors from UX-017 still show on the field

## Tests

- Automated: `GroupNameHelperTest.canSubmitRejectsBlankNames`, Paparazzi `CreateGroupDialogPaparazziTest`

## Fallback validation

- Command: `./gradlew :core-domain:test --tests com.multiappshare.domain.GroupNameHelperTest`
