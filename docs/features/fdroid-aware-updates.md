# Feature: fdroid-aware-updates

> F-Droid installs must never be offered a GitHub APK. Sideload keeps Install. Spec for BUILD_PLAN **AE.1**.

## Acceptance criteria

- ✅ User-visible: newer-version prompt still appears on F-Droid; confirm opens the F-Droid listing (`ACTION_VIEW`), not an APK URL
- ✅ Sideload / unknown installer still uses the GitHub APK URL and **Install**
- ✅ Donate stays off the update dialog
- ✅ Offline/error: failed installer lookup treats as sideload; fetch failures stay silent
- ✅ i18n: `update_open_fdroid` / `update_available_body_fdroid` in `en` / `es` / `fr`

## Smoke scenario

1. _Given_ installer package `org.fdroid.fdroid` and a newer GitHub APK
2. _When_ the daily check prompts
3. _Then_ the confirm URL is `https://f-droid.org/packages/com.edwardlthompson.multiappshare/`

## Container map

| Layer | Path |
|-------|------|
| Logic | `core-domain/.../updates/InstallChannel.kt` |
| View | `AppUpdatesHost` button/body swap |
| Tests | `InstallChannelTest` + LaunchPrompt F-Droid case |
| Wiring | `AppUpdates.evaluate` ≤10 lines |

## Tests

- Automated: yes — installer allowlist + listing URL

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`

## Definition of Done

See `docs/FEATURE_MODULES.md`. One `/feature` task. BUILD_PLAN **AE.1**.
