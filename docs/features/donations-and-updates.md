# Feature: donations-and-updates

Quiet Venmo support and a once-daily GitHub APK check, matching Continuum Calendar.

## Acceptance criteria

- 🔲 **Donate via Venmo** is always available in About and the app menu. It is never on the update dialog.
- 🔲 First run records the installed version and does not show a donate popup.
- 🔲 After a later launch where the installed version changed, one optional note appears (title **Development is still going**). Either button records “seen this version.”
- 🔲 Update check runs at most once per 24 hours, compares product APK filenames (not git/template tags), and prompts **Install** | **Later**. Later silences that version.
- 🔲 Failed fetch, timeout, empty assets, or same version stay silent and never block the app.
- 🔲 Automatic donate/update dialogs run on **release** builds; debug skips them so instrumented tests stay quiet. Menu/About Venmo still works on debug.
- 🔲 Donate prefs and last-check timestamps stay device-local (not in encrypted backup export).
- 🔲 i18n: `about_donate`, `donate_*`, `update_*` in `en` / `es` / `fr`.

## Smoke scenario

1. _Given_ a sideload install of the current APK
2. _When_ the user opens Menu → **Donate via Venmo** or About
3. _Then_ the public Venmo page opens via `ACTION_VIEW`, with no update dialog mixed in

## Container map

| Layer | Path |
|-------|------|
| Logic | `core-domain/.../updates/` (`ProductUpdate`, `LaunchPromptDecider`, `GithubReleaseJson`) |
| Prefs / HTTP | `app/.../updates/` (`UpdatePrefs`, `GithubReleaseFetcher`, `AppUpdates`) |
| View | `feature-dashboard/.../DashboardSupportDialogs.kt`, `app/.../ui/main/AppUpdatesHost.kt` |
| Tests | `core-domain/src/test/.../updates/` |
| Wiring | `MainScreenSuccessBody` → `AppUpdatesHost` (one line) |

## Definition of Done

See `docs/FEATURE_MODULES.md` per-feature checklist and BUILD_PLAN **AC**.
