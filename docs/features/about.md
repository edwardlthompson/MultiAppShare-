# Feature: about

> Golden Path About screen for this android child. Port into existing About/donate surfaces. **Do not** copy `examples/android/` over `app/`. Stub: template `docs/features/donations-updates.md`. Overlaps shipped **AC** (`donations-and-updates.md`).

## Acceptance criteria

- 🔲 User-visible: a dedicated About surface (version, license/privacy, changelog, Donate via Venmo) reachable from the overflow menu
- 🔲 Donate stays off the update/install dialog (AC invariant)
- 🔲 Offline/error: About renders with no network; failed update check stays silent
- 🔲 Accessibility: dialog/screen is labelled; donate is a real button/link
- 🔲 i18n: `about_*` in `en` / `es` / `fr`

## Smoke scenario

1. _Given_ the app is on the dashboard
2. _When_ the user opens Menu → About
3. _Then_ version and Venmo donate are visible, with no update dialog mixed in

## Container map

| Layer | Path |
|-------|------|
| Logic | `core-domain/.../updates/` (already shipped) |
| View | `feature-dashboard/.../DashboardDialogs.kt` (`DashboardAboutDialog`); extend, do not replace AC |
| Tests | `core-domain/src/test/.../updates/` + About UI unit/Paparazzi as needed |
| Wiring | `MainScreenContent` / overflow ≤10 new lines |

## Tests

- Automated: yes — extend existing `ProductUpdate` / About dialog tests

## Fallback validation

- Why tests are not feasible: N/A (automated tests exist)
- Command: `bash scripts/feature-gate.sh --stack android`

## Definition of Done

See `docs/FEATURE_MODULES.md`. One `/feature` task. BUILD_PLAN **AD.1**.
