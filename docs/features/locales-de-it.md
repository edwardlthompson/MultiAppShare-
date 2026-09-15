# Feature: locales-de-it

> German and Italian match domain, picker, locale-config, and resource folders. BUILD_PLAN **UX-014**.

## Acceptance criteria

- ✅ `AppLanguageTags.pickerOrder` is en, de, es, fr, it
- ✅ `LanguageDialog` lists every picker tag
- ✅ `locales_config.xml` lists the same tags
- ✅ `values-de/strings.xml` and `values-it/strings.xml` include every English key

## Tests

- Automated: `AppLanguageTagsTest.pickerOrderMatchesSupportedSet`, `LocaleParityTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
