# Feature: locale-de-it

> German (de) and Italian (it) language tag and locale parity support. BUILD_PLAN **AE.50**.

## Acceptance criteria

- ✅ `AppLanguageTags.supported` includes `"de"` and `"it"` alongside English, French, and Spanish
- ✅ `AppLanguageTags.sanitize` normalizes and validates German and Italian BCP 47 language tags
- ✅ Fully local locale selection

## Tests

- Automated: `AppLanguageTagsTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
