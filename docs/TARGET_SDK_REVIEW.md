# targetSdk review (Milestone **M.3** / **V.3**)

Use this checklist **before** each **`targetSdk` / `compileSdk` bump** (ideally quarterly or with the annual platform release).

## Current stack (recorded)

| Setting | Value |
|--------|--------|
| `compileSdk` | 37 |
| `targetSdk` | 36 (bumped 2026-06-19, V.3b) |
| `minSdk` | 26 |
| Gradle | 9.5.1 (wrapper) |
| AGP | 9.2.1 (see `gradle/libs.versions.toml`) |
| Kotlin | 2.4.0 |
| JDK | 21 |

## V.3 review — 2026-06-19 (targetSdk **36** readiness)

**Reviewer:** `[AGENT]` · **Verdict:** **Defer `targetSdk` 36** until edge-to-edge share-overlay pass on Android 16 hardware. No manifest bump in this sprint.

### Area checklist (API 36 / Android 16)

| Area | Status | Notes |
|------|--------|-------|
| **Edge-to-edge** | ✅ V.3b | `enableEdgeToEdge()` in `MainActivity`; M3 `Scaffold` padding; **Android 16 smoke pass** (CPH2583, 2026-06-19). |
| **Predictive back** | ✅ | `android:enableOnBackInvokedCallback="true"` on `MainActivity`; onboarding/dialog flows documented in M.3. |
| **Notifications** | ✅ | `POST_NOTIFICATIONS` declared; runtime request in `MainActivity`; denial paths in ViewModel. `SharingService` uses low-importance channel `sharing_service_channel_v2`. |
| **Foreground service** | ✅ | `foregroundServiceType="dataSync"` + `FOREGROUND_SERVICE_DATA_SYNC`; `startForeground(..., DATA_SYNC)` on API 34+. User-initiated share only; `START_NOT_STICKY`. Review FGS timeout rules when targeting 36. |
| **Backups / data** | ✅ | `data_extraction_rules` + `fullBackupContent`; encrypted export separate from Auto Backup ([`BACKUP_FORMAT.md`](BACKUP_FORMAT.md)). |
| **Locale / RTL** | ✅ | `android:localeConfig="@xml/locales_config"`; `values-es` present. |
| **Permissions (health/wear)** | ✅ N/A | No `BODY_SENSORS` / health FGS — phone/tablet scope only. |
| **16 KB page size** | ✅ | Documented [`NATIVE_16KB_PAGE_SIZE.md`](NATIVE_16KB_PAGE_SIZE.md); re-run after native dep upgrades. |
| **Lint / compile** | ✅ | `compileSdk` 37 · `targetSdk` 36 (V.3b). |

### Recommendation (superseded by V.3b — 2026-06-19)

| Decision | Outcome |
|----------|---------|
| **`targetSdk` 36** | ✅ Applied in V.3b — `enableEdgeToEdge()` in `MainActivity`; `app/` + `baselineprofile/` bumped. |
| **Device smoke** | ✅ | CPH2583 / Android 16 — instrumented + ADB overlay/rotation/deeplink; see [`GATES.md`](GATES.md). Full [`RELEASE_SMOKE.md`](RELEASE_SMOKE.md) before next `v*` tag. |

### V.3b implementation (2026-06-19)

- `MainActivity.onCreate`: `enableEdgeToEdge()` before `super.onCreate()` (API 21+ back-compat; required for API 36).
- `targetSdk` **36** in `app/build.gradle.kts` and `baselineprofile/build.gradle.kts`.
- Edge-to-edge: M3 `Scaffold` content padding unchanged; translucent share theme retained in `core-ui` themes.

---

## Pre-bump checklist (use before any future bump)

1. **Release notes** — Read [Behavior changes: all apps](https://developer.android.com/about/versions) for the new API level.
2. **Manifest** — `foregroundServiceType`, `POST_NOTIFICATIONS`, `localeConfig`, `enableOnBackInvokedCallback`.
3. **Notifications** — Channels, runtime permission denial paths (`SharingService`).
4. **Back navigation** — Predictive back + dialogs (`ui/onboarding/OnboardingDialog`).
5. **Data & backups** — `data_extraction_rules` / `fullBackupContent` vs Room.
6. **Testing** — `./gradlew :app:lintDebug`, `test`, `assembleDebug`, `assembleRelease`; device smoke per [`RELEASE_SMOKE.md`](RELEASE_SMOKE.md).

## After bump

- Add a row to the **Current stack** table (new `targetSdk` + date).
- Log in [`docs/DECISION_LOG.md`](DECISION_LOG.md) and [`docs/GATES.md`](GATES.md).

## References

- [Target API requirements (Play)](https://support.google.com/googleplay/android-developer/answer/11926878)
- [Migrate your app](https://developer.android.com/about/versions)
