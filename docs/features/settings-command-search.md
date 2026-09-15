# Feature: settings-command-search

> Settings search finds rows like haptic and delay. BUILD_PLAN **UX-067**.
>
> Layer: user-visible (`SettingsScreen`). Domain: `SettingsCatalog.search`.

## Acceptance criteria

- ✅ `SettingsScreen` search field uses `SettingsCatalog.search` with localized titles
- ✅ Typing “haptic” or “delay” matches catalog ids
- ✅ Empty results use `settings_no_results`

## Tests

- Automated: `SettingsCatalogTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
