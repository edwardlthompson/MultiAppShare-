# Feature: share-session-keys

> Enter continues sharing; Esc closes the overlay; system Back is unchanged. BUILD_PLAN **UX-056**.

## Acceptance criteria

- ✅ `ShareSessionKeys.action("Enter")` is Next unless paused
- ✅ Escape cancels the overlay; `"Back"` is `IGNORE` (`INTERCEPTS_BACK` is false)
- ✅ About documents the shortcuts (`about_shortcuts`)

## Tests

- Automated: `ShareSessionKeysTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
