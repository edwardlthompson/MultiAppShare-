# Feature: settings

> Golden Path Settings slice. Port into existing theme/settings host. **Do not** copy `examples/android/` over `app/`. Stub: template `docs/features/settings.md`.

## Acceptance criteria

- ✅ User-visible: Settings (or equivalent) from main navigation; theme light/dark/system persists
- ✅ Optional **Save crash details for me to review** toggle (default off) — wires later to AD.3
- ✅ Daily GitHub installer checks are not gated here (see `donations-and-updates.md`)
- ✅ Offline: last persisted values; no network required
- ✅ i18n: `settings_*` / existing `theme_*` in `en` / `es` / `fr`

## Smoke scenario

1. _Given_ default theme
2. _When_ the user opens Theme/Settings and chooses dark
3. _Then_ dark theme applies immediately and survives process death

## Container map

| Layer | Path |
|-------|------|
| Logic | `SettingsRepository` in `:core-domain` / `:core-database` |
| View | `app/.../ui/settings/ThemeDialog.kt`, `MainScreenSettingsHost.kt` |
| Tests | existing settings/theme unit tests + toggle persistence |
| Wiring | `MainScreenOverflowMenu` ≤10 new lines |

## Tests

- Automated: yes — persist theme + crash-save default-off

## Fallback validation

- Why tests are not feasible: N/A (automated tests exist)
- Command: `bash scripts/feature-gate.sh --stack android`

## Definition of Done

See `docs/FEATURE_MODULES.md`. One `/feature` task. BUILD_PLAN **AD.2**.
