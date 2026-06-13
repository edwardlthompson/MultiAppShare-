# targetSdk review (Milestone **M.3**)

Use this checklist **before** each **`targetSdk` / `compileSdk` bump** (ideally quarterly or with the annual platform release).

## Current stack (recorded)

| Setting | Value |
|--------|--------|
| `compileSdk` | 37 |
| `targetSdk` | 35 |
| `minSdk` | 26 |
| Gradle | 9.5.1 (wrapper) |
| AGP | 9.2.1 (see `gradle/libs.versions.toml`) |
| Kotlin | 2.4.0 |
| JDK | 21 |

## Pre-bump checklist

1. **Release notes** — Read [Behavior changes: all apps](https://developer.android.com/about/versions) for the new API level (notifications, FGS types, **exact alarms**, **edge-to-edge**, backup rules, etc.).
2. **Manifest** — `foregroundServiceType`, `POST_NOTIFICATIONS`, **`localeConfig`**, **`enableOnBackInvokedCallback`** (predictive back), any new permission splits.
3. **Notifications** — Channels, runtime permission denial paths (`SharingService` / compatibility alerts).
4. **Back navigation** — Predictive back + `Dialog` / full-screen onboarding (`ui/onboarding/OnboardingDialog`); no accidental double-finish.
5. **Data & backups** — `data_extraction_rules` / `fullBackupContent` vs Room (**F.4**); encrypted export story unchanged (**N**).
6. **Testing** — `./gradlew :app:lintDebug`, `test`, `assembleDebug`, `assembleRelease`; device smoke: share sequence, backup import/export, deeplink.

## After bump

- Add a **short bullet** to this file’s table (new `targetSdk` + date).
- Update **`docs/BUILD_PLAN.md`** **M.3** evidence line if the plan still references an older level.

## References

- [Target API requirements (Play)](https://support.google.com/googleplay/android-developer/answer/11926878)
- [Migrate your app](https://developer.android.com/about/versions) (per API level)
