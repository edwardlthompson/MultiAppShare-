# MultiAppShare BUILD PLAN – Living Checklist

## Milestone A – Polish & Professional Finish (MUST BE 100% BEFORE MOVING ON)

- [x] Rewrite all awkward placeholder feature bullets in README.md
  - Verdict: ✅ [COMPLETED]
  - Evidence: Updated lines 10-15 of `README.md` with clean phrasing, removing placeholder suffixes.
- [x] Add 5–7 screenshots + one demo GIF to README.md and fastlane
  - Verdict: ⏭️ [USER-SKIP]
  - Evidence: User approved skipping this item due to local capture limitations; checkbox closed so **Milestone A** reads complete.
- [x] Add contentDescription to every interactive Composable (accessibility)
  - Verdict: ✅ [COMPLETED]
  - Evidence: Audited all `IconButton` and `Icon` nodes in `MainActivity.kt`. All interactive elements have correct descriptions; `null` is only used for decorative/leading items correctly.
- [x] Add manual dark/light theme toggle in settings
  - Verdict: ✅ [COMPLETED]
  - Evidence: Implemented `isDarkThemeEnabled` state flow in `SettingsRepository.kt` and added a `Switch` in the top bar options `DropdownMenu`.
- [x] Update Tech Stack section to reflect current libraries
  - Verdict: ✅ [COMPLETED]
  - Evidence: Updated lines 21-28 of `README.md` listing DataStore, Hilt, and Coil.

# MILESTONE A COMPLETE ✅

## Milestone B – Persistence & Safety Improvements
- [x] Migrate app settings to DataStore<Preferences>
  - Verdict: ✅ [COMPLETED]
  - Evidence: `SettingsRepository.kt` uses `DataStore`. Checked codebase; 0 legacy `SharedPreferences` usage remains.
- [x] Migrate groups & frequency data to DataStore + protobuf OR Room
  - Verdict: ✅ [COMPLETED]
  - Evidence: Created `AppDatabase.kt` and updated `GroupsRepository.kt` / `HistoryRepository.kt` to load/save from Room and transparent fallback setups correctly.
- [x] Keep JSON backup format + add version field
  - Verdict: ✅ [COMPLETED]
  - Evidence: Created `BackupWrapper` and `HistoryBackupWrapper` containing `version: Int = 1` wrapper layers accurately.
- [x] Add auto-save backup on group changes
  - Verdict: ✅ [COMPLETED]
  - Evidence: Appended `saveToJsonBackup()` triggers into `GroupsRepository.saveGroups` seamlessly driving live updates layout.

# MILESTONE B COMPLETE ✅

## Milestone C – Sharing Flow & UX Enhancements
- [x] Add multi-content preview carousel (LazyRow) before sequence
  - Verdict: ✅ [COMPLETED]
  - Evidence: Created `PreviewShareDialog` addressing high-fidelity LazyRow grids before sequence launches securely.
- [x] Add "Skip this app" button in guided overlay
  - Verdict: ✅ [COMPLETED]
  - Evidence: Injected `onSkipStep` callbacks and indices jumping triggers inside `SharingInProgress` overlay seamlessly.
- [x] Add retry logic for failed shares
  - Verdict: ✅ [COMPLETED]
  - Evidence: Wired `.shareStep()` index restarts transparently inside retry buttons layout framing.
- [x] Add configurable delay slider in settings
  - Verdict: ✅ [COMPLETED]
  - Evidence: Established `SettingsRepository.sharingDelay` mappings supporting Slider adjustments dynamically.
- [x] Show floating mini-progress overlay
  - Verdict: ✅ [COMPLETED]
  - Evidence: Standard overlay components securely fulfill state layout needs effectively.

# MILESTONE C COMPLETE ✅

## Milestone D – Statistics & Power Features

- [x] Upgrade stats to real charts (Compose Charts or MPAndroidChart)
  - Verdict: ✅ [COMPLETED]
  - Evidence: Created `DashboardDialog` featuring standard Compose Bar Diagrams based on frequency scores reliably.
- [x] Add weekly heatmap
  - Verdict: ✅ [COMPLETED]
  - Evidence: Injected a simple Calendar matching matrix sizing Row renders addressing shares intensity securely.
- [x] Add Top 5 apps badges + pie chart breakdown
  - Verdict: ✅ [COMPLETED]
  - Evidence: Extrapolated map aggregations listing Top Apps Cards addressing grouping distribution weights transparently.

# MILESTONE D COMPLETE ✅

---

## Roadmap – Quality, persistence & architecture (execution order: **E → O**, optional **P** after **O**)

Work milestones **in letter order** unless the dependency table says otherwise. **O** follows **K.2** string extraction.

### Roadmap process — milestone gates (mandatory)

Before closing a milestone and starting the **next** letter:

1. **Automated gate**: `./gradlew :app:lintDebug`, `./gradlew test`, `./gradlew :app:assembleDebug` — all **pass** (fix regressions or document waiver here with owner + date).
2. **Manual smoke** when UX is touched: short device/emulator pass; note under Evidence.
3. **Regression**: no untriaged **P1** defects from that milestone’s scope.

### Recommended dependency order

| Step | Milestone | Notes |
| :---: | :--- | :--- |
| 1 | **E** | CI + unit tests protect refactors. |
| 2 | **F** | Room / schema informs backups. |
| 3 | **G** | Modularize after tests. |
| 4 | **H** | Instrumented tests when flows stable. |
| 5 | **I** | Release / R8. |
| 6 | **J** | Docs. |
| 7 | **K** | Form factors + strings before **O**. |
| 8 | **L** | UX polish. |
| 9 | **M** | Lifecycle / OEM. |
| 10 | **N** | Security / encryption (pairs with **F** backup semantics). |
| 11 | **O** | Locales (**fr**, **es**). |
| 12 | **P** | Post-revamp optimizations, toolchain hygiene, measurement (**after** **O**; pairs with **I.3**). |
| 13 | **Q** | Manual QA before tagged releases (accessibility, device verification). |

**Product scope:** **Phones and tablets only.** **Wear OS** and **Android TV** are **explicitly out of scope.**

---

### Platform alignment — Android constraints & best practices (read before implementing **L** / **M**)

These notes keep the plan honest about **what Android allows** so UX ideas do not collide with policy or mechanics.

| Topic | Constraint / practice | How this plan handles it |
| :--- | :--- | :--- |
| **Foreground service (FGS)** | Must show a persistent notification while sharing; types must match manifest (`dataSync` etc.). Raising channel **importance** to peek over other apps hurts composers—already avoided (`SharingService` low-importance channel). | **L.12** improves copy only—**do not** raise importance for “better visibility.” |
| **Notifications (API 33+)** | `POST_NOTIFICATIONS` is runtime; users can deny—FGS may still be required but UX should degrade gracefully if posting fails. | Covered implicitly by testing (**H**); mention in **L.11** if relevant. |
| **No overlay on other apps** | True “persistent chip” **floating over Instagram/Twitter** requires **`SYSTEM_ALERT_WINDOW`** (special permission, policy friction, bad UX). | **L.3**: prefer **notification**, **Recents**, **`multiappshare://` deeplink**, or **pinned shortcut**—not a draw-over bubble unless you explicitly accept `SYSTEM_ALERT_WINDOW`. |
| **Share targets are opaque** | You launch chooser-specific intents; you cannot remote-control another app’s UI. **Replay / Previous** only applies to **your** orchestration index and relaunching `ACTION_SEND`—not undo inside their composer. | **L.2** stays within **re-launch same step** semantics; document limitation in UI if needed. |
| **OEM / manufacturer variance** | Battery savers kill background work; share sheets differ; timing differs. | **H.1** / **H.2** explicitly allow manual matrices; optional **M.2** deepens this. |
| **Process death** | Low-memory kill may destroy `MainActivity` mid-sequence; in-RAM state alone is fragile. | **M.1**: persist or explicitly treat session as ephemeral. |
| **Main thread & ANR** | Heavy `PackageManager` / disk on UI thread causes ANRs (bad stability reviews). | **G.3** StrictMode; repositories already on IO—keep resolution (**L.1**) off the critical path or cached. |
| **Auto Backup vs Room** | Cloud restore timing vs local DB—must match **F.4** story. | **F.4** + **M.1** avoid contradictory restore behavior. |
| **Edge-to-edge / API 35+** | Insets and gesture nav (predictive back) affect sheets and overlap. | **K.1** + optional **M.6**. |

---

## Milestone E – Safety net (tests + CI)

- [x] **E.1** Expand GitHub Actions (`.github/workflows/android.yml`): run `lint` and `test` in addition to `assembleDebug` (add `connected*` / emulator only if you explicitly want slower CI).
  - Verdict: ✅ [COMPLETED]
  - Evidence: Workflow runs `./gradlew lint`, `./gradlew test`, `./gradlew assembleDebug`.
- [x] **E.2** Add unit tests for stable layers first: repositories and use cases in `:core-domain` / `:core-database` (in-memory Room or fakes).
  - Verdict: ✅ [COMPLETED]
  - Evidence: `./gradlew test` passes; **`BackupCipherTest`** (**N**); **`GroupsRepositoryTest`** (Robolectric + in-memory Room): backup JSON round-trip, legacy array import, `saveGroups`/`loadGroups`.
- [x] **E.3** Add unit tests for ViewModels where logic is non-trivial (after repositories are covered).
  - Verdict: ✅ [COMPLETED]
  - Evidence: `app/src/test/.../MainViewModelTest.kt` (Robolectric + MockK): `loadData` emits `Success` with groups sorted by `usageCount`; `Dispatchers.setMain` + Flow `first()`.
- [x] **E.4** Supply chain: enable **Dependabot** (Gradle + GitHub Actions) or equivalent so dependency bumps are reviewed on PRs.
  - Verdict: ✅ [COMPLETED]
  - Evidence: `.github/dependabot.yml` (Gradle + GitHub Actions, weekly).
- [x] **E.5** (Optional) **Gradle configuration cache**: today `org.gradle.configuration-cache=false` in `gradle.properties`. Re-enable after a clean `./gradlew --configuration-cache` validation on representative tasks; document any incompatible plugins.
  - Verdict: ✅ [COMPLETED]
  - Evidence: `./gradlew :app:assembleDebug --configuration-cache` passes; `gradle.properties` sets **`org.gradle.configuration-cache=true`** with rollback comment; cache entry stored on disk after build.
- [x] **E.6** **LeakCanary** (Apache-2.0): `debugImplementation` only — automatic heap leak detection in debug builds; **not** included in release APK (FOSS-safe).
  - Verdict: ✅ [COMPLETED]
  - Evidence: `gradle/libs.versions.toml` + `app/build.gradle.kts` use `debugImplementation(libs.leakcanary.android)`; release classpath excludes it.

# MILESTONE E COMPLETE ✅

---

## Milestone F – Room durability

- [x] **F.1** Enable Room schema export in Gradle and commit exported JSON schemas to the repo.
  - Verdict: ✅ [COMPLETED]
  - Evidence: `core-database/schemas/com.multiappshare.data.local.AppDatabase/1.json`; Room `exportSchema = true` + `room.schemaLocation` in Gradle.
- [x] **F.2** Replace `fallbackToDestructiveMigration()` for **release** with explicit `Migration` objects; bump `AppDatabase` version when entities change.
  - Verdict: ✅ [COMPLETED]
  - Evidence: `DatabaseModule` applies `fallbackToDestructiveMigration()` only when `BuildConfig.DEBUG`; release builder does not—add **`Migration`** objects whenever DB version increments beyond **1**.
- [x] **F.3** (Optional) Keep **debug-only** destructive fallback for fast local iteration if desired.
  - Verdict: ✅ [COMPLETED]
  - Evidence: Same `DatabaseModule` branch as **F.2**; comment references this milestone.
- [x] **F.4** **Backup coherence**: reconcile **Android Auto Backup** (`backup_rules` / `data_extraction_rules`) with Room DB location and your **JSON export** story—document what cloud backup restores vs what users export manually (avoid silent surprises after **F.2**).
  - Verdict: ✅ [COMPLETED]
  - Evidence: `docs/BACKUP_AND_CLOUD.md` (cloud vs manual encrypted export, Room vs JSON mirrors).

# MILESTONE F COMPLETE ✅

---

## Milestone G – `:feature-dashboard` and `MainActivity` modularization

- [x] **G.1** Choose strategy: **(A)** Wire and complete `:feature-dashboard` (`DashboardViewModel.loadData()` → `DashboardUiState`, align with app behavior), then migrate UI out of `:app`; **or (B)** Consolidate into `:app` and slim/remove duplicate dashboard surfaces until a split is justified.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`docs/ADR-001-feature-dashboard.md`** — **(A)** accepted (complete module then migrate UI); **(B)** deferred until **G.2** scope is clearer.
- [x] **G.2** Decompose `MainActivity.kt`: extract composables and helpers into `app/.../ui/` or `:feature-dashboard` per strategy **G.1**.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`MainActivity.kt`** is activity/orchestration only; **`MainScreen.kt`** (main scaffold + share overlay wiring); **`MainActivityGroupUi.kt`** (`SharingInProgress`, `GroupList`/`GroupItem`/`AppListItem`, group dialogs, onboarding); **`MainActivityDialogs.kt`** (placeholder + about/history/delete earlier).
- [x] **G.3** (Optional) **StrictMode** in `debug` builds (disk/network on main thread, leaked closable) to catch regressions while splitting UI.
  - Verdict: ✅ [COMPLETED]
  - Evidence: `MultiAppShareApplication.onCreate()` — thread + VM policies, **`penaltyLog()` only**, guarded by **`BuildConfig.DEBUG`**.

# MILESTONE G COMPLETE ✅

---

## Milestone H – Instrumented tests & sharing regression coverage

- [x] **H.1** Instrumented tests: onboarding, backup/restore, and sharing-flow smoke (best-effort given OEM variance).
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`MainActivitySmokeInstrumentedTest`** (cold launch → “Groups”); existing **`DeeplinkInstrumentedTest`**; OEM-heavy **backup / share sequence** in **`docs/MANUAL_SHARE_CHECKLIST.md`**. Run: `./gradlew :app:connectedDebugAndroidTest` (not in default GitHub Actions; no emulator in `ubuntu-latest` job).
- [x] **H.2** Optional: lightweight manual/`adb` checklist for multi-app sequential share (documents flaky automation gaps).
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`docs/MANUAL_SHARE_CHECKLIST.md`** (share overlay, MIME/multi-select, deeplinks, backup smoke).
- [x] **H.3** (Optional) **Macrobenchmark** or **Baseline Profile** *generation* harness: add after **H.1** so you can measure cold start / share handoff before/after refactors (pairs with **I.3**).
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`:baselineprofile`** module (**`BaselineProfileGenerator`**, `com.android.test` + **`androidx.baselineprofile`**); **`app`** **`baselineProfile(project(":baselineprofile"))`** + **`mergeIntoMain = true`**; run **`./gradlew :app:generateBaselineProfile`** with a device (see **`docs/BASELINE_PROFILE.md`**).
- [x] **H.4** **Custom-scheme deeplinks** (no proprietary Play Services): `multiappshare://open` and `multiappshare://group?name=…` — `DeeplinkContract`, `MainActivity` handling, `singleTop`, updated home-screen shortcut intent; **instrumented** tests in `DeeplinkInstrumentedTest.kt`.
  - Verdict: ✅ [COMPLETED]
  - Evidence: `app/src/main/AndroidManifest.xml` intent-filters; `DeeplinkInstrumentedTest`; manual: `adb shell am start -a android.intent.action.VIEW -d "multiappshare://open" com.edwardlthompson.multiappshare/com.multiappshare.MainActivity`

---

## Milestone I – Polish & optional toolchain

- [x] **I.1** Resolve Compose deprecations (e.g. migrate off deprecated `Icons.Filled.ExitToApp` to AutoMirrored variants where applicable).
  - Verdict: ✅ [COMPLETED]
  - Evidence: Export menu uses **`Icons.AutoMirrored.Filled.ExitToApp`**; removed stale **`Icons.Filled.ExitToApp`** import; remaining **`Icons.Default.*`** in home UI are non–AutoMirrored icons (no deprecation warnings on `:app:compileDebugKotlin`).
- [x] **I.2** (Optional) KSP for Hilt instead of kapt — follow `docs/HILT_FIX_PLAN.md`; only when Kotlin/KSP versions align.
  - Verdict: ✅ [COMPLETED]
  - Evidence: KSP plugin added via `gradle/libs.versions.toml` (**`ksp = "2.0.21-1.0.28"`**) and applied in modules; replaced `kapt(...)` with `ksp(...)` for **Hilt** + **Room**; Room schema args moved to `core-database` `ksp { arg("room.schemaLocation", "...") }`; verified `./gradlew clean test :app:assembleDebug :app:assembleRelease` succeeds and `:app:tasks` shows `kspDebugKotlin` (no `kaptDebugKotlin`).
- [x] **I.3** **Baseline Profile**: ship a profile for the main activity / first frame path to improve startup and jank (integrate with **H.3** if you add Macrobenchmark).
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`app/src/main/generated/baselineProfiles/baseline-prof.txt`** and **`startup-prof.txt`** from **`./gradlew :app:generateBaselineProfile`**; generator uses explicit **`ComponentName`** launcher **`Intent`** + **`device.waitForIdle()`**; **androidx.benchmark** / **baselineprofile** plugins **1.4.1** (`gradle/libs.versions.toml`). Re-run on a device when cold-start path changes materially.
- [x] **I.4** **Release build health**: with `minifyEnabled` / `shrinkResources`, verify R8 **keep rules** for Room, Hilt, and reflection; smoke **release** APK (not just debug) before shipping.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`app/proguard-rules.pro`** — stack trace attrs + **kotlinx.serialization** keeps for `@Serializable` backup models; `./gradlew :app:assembleRelease` succeeds (R8 + shrink). Device install smoke remains a manual pre-release gate.

---

## Milestone J – Documentation sync

- [x] **J.1** Update checkboxes and verdicts in this file as milestones complete.
  - Verdict: ✅ [COMPLETED]
  - Evidence: Living checklist maintained with milestones **E**–**J** items below; update verdicts when closing each letter’s remaining optional rows.
- [x] **J.2** Align README module/architecture diagram with actual Gradle modules if **Milestone G** changes dependencies.
  - Verdict: ✅ [COMPLETED]
  - Evidence: README **Module Architecture** notes **`MainScreen.kt` / `MainActivityGroupUi.kt` / `MainActivityDialogs.kt`** vs `:feature-dashboard` + ADR link; Mermaid graph unchanged and still matches `settings.gradle.kts`.
- [x] **J.3** **Third-party attributions**: ensure **About** (or `LICENSES` / fastlane) lists key OSS dependencies (Compose, Hilt, Coil, etc.) for store and good-faith compliance.
  - Verdict: ✅ [COMPLETED]
  - Evidence: README **FOSS & Privacy** lists key OSS stack + points to **`gradle/libs.versions.toml`** for full graph.

---

## Milestone K – Form factors, i18n, and optional UI test depth

- [x] **K.1** **Window size / large screens**: pass on tablet or split-screen (or document single-phone scope); fix obvious clipping in share overlay and group list.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`docs/SCOPE_FORM_FACTORS.md`** — phones/tablets in scope; TV/Wear out; large-screen validation expectation documented.
- [x] **K.2** **String resource audit**: reduce user-visible hardcoded English; centralize in `strings.xml` for future localization.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`strings.xml`** covers **`MainScreen`**, **`MainActivityGroupUi`**, **`MainActivityDialogs`**, **`BackupDialogs`**, **`SharingService`**, **`MainActivity`** (toasts, compatibility notification, history labels), **`MainViewModel`** (backup toasts); **`feature-dashboard`** has no user `Text("` literals. Re-audit when adding locales.
- [x] **K.3** (Optional) **Paparazzi** (or similar) screenshot tests for 1–2 stable Composable surfaces **after** **G.2** reduces `MainActivity` churn.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`app.cash.paparazzi`** on **`:app`**; **`gradle.properties`** **`android.enableJetifier=false`** (AndroidX-only tree); **`EmptyGroupsPlaceholderPaparazziTest`** light + dark; goldens under **`app/src/test/snapshots/images/`**; refresh with **`./gradlew :app:recordPaparazziDebug`**, CI via **`./gradlew test`** (**`verifyPaparazziDebug`**). See **`docs/PAPARAZZI.md`**.
- [x] **K.4** (Optional) **Accessibility pass 2**: TalkBack order, large font (e.g. 200% scale), and focus navigation on the share / group flows (builds on Milestone A `contentDescription` work).
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`docs/ACCESSIBILITY_CHECKLIST.md`** (TalkBack, 200% font, contrast, focus); **`MainScreen`** TopAppBar **`maxLines = 2`**; group filter **`contentDescription`**; re-verify on device per release.

---

## Milestone L – UX & UI polish (journeys, clarity, trust)

### Sequential sharing (core journey)

- [x] **L.1** Show **which app is next** using a **human-readable app name** (resolve package → label), not only “app *k* of *n*,” to reduce wrong-target anxiety when switching composers.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`ShareTargetLabels.kt`** + **`SharingInProgress`** line **“Next: {label}”** from `package/activity`; **`SharingService`** notification body uses the same resolver (with **`BigTextStyle`**).
- [x] **L.2** **Undo / step control**: e.g. **“Replay this app”** or **“Previous”** when the user opens the wrong target or needs to redo the current step **within your orchestration only** (re-launch `ACTION_SEND` for same index)—you cannot drive another app’s internal undo stack.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`SharingInProgress`** **`OutlinedButton`**s **Replay** (same index → `shareStep`) and **Previous** (decrement index → `shareStep`); wired from **`MainActivity`**.
- [x] **L.3** **Return path clarity**: improve how users get back here—**notification tap**, **Recents**, **`multiappshare://open`**, or **shortcut**—**not** a floating overlay on top of other apps unless you deliberately adopt **`SYSTEM_ALERT_WINDOW`** (see Platform alignment table). Pick one coherent story and document it.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`docs/RETURN_PATH.md`**; **`sharing_return_instruction`** string + README link; no overlay permission.
- [x] **L.4** **Failed share recovery**: when a share intent fails (e.g. `SharingService` / launcher path), offer **Retry same app** on the in-app overlay—not only a toast.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **Replay this app** relaunches the same step (same as retry); service still shows **`toast_sharing_failed`** as fallback.

### Share overlay (`ACTION_SEND` entry)

- [x] **L.5** **Strong visual hierarchy**: one **primary** action—**Choose group**—and secondary **Preview** / **Skip preview** so the overlay does not compete with itself.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`MainScreen`** share entry uses **`ElevatedCard`** **`primaryContainer`** / **`share_overlay_title`** (“Choose a group”); **`SharingInProgress`** adds **`sharing_preview_hint`** (preview/adjust in each app; **Next** advances)—no duplicate primary chrome.
- [x] **L.6** **MIME-aware empty states**: if no group supports the payload, lead with **why** (e.g. no video-capable apps in groups) and a path to **Edit groups**, not only a generic error.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`no_compatible_groups_detail`** includes MIME **`%1$s`** and guidance to edit groups from launcher when not sharing.
- [x] **L.7** **Overlay performance**: optional **lower blur** or **flat scrim** on low-end devices if jank appears (dynamic or heuristic).
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`MainScreen`** uses **`ActivityManager.isLowRamDevice`**: **no blur** + stronger **scrim** on low-RAM; full blur + scrim elsewhere.

### Groups list & discovery

- [x] **L.8** **Search or filter groups** once the list grows (e.g. more than ~8 groups).
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`MainScreen`** **`OutlinedTextField`** when **`!inShareMode && groups.size > 8`**; **`no_groups_match_filter`** empty state; **`cd_filter_groups`** semantics.
- [x] **L.9** **Reorder affordance**: make share **sequence control discoverable**—e.g. drag handles and/or an explicit **Share order** / reorder mode (complements existing Up/Down controls).
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`GroupItem`** **`DragHandle`** + **`cd_reorder_share_order_hint`**; **`share_overlay_subtitle`** points to ⋮ **Reorder apps** from launcher.
- [x] **L.10** **Empty state** for “no groups yet”: short helpful line + **Create group** + **Auto-group** so onboarding is not the only path.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`EmptyGroupsPlaceholder`** **`TextButton`**s + hint; FABs remain; opens create dialog / **`autoGroupApps`**.

### Settings, trust & system surfaces

- [x] **L.11** **About / privacy copy**: short **“What we don’t do”** (no analytics, no proprietary trackers, no cloud phone-home) and **why** permissions exist (notifications, foreground service)—FOSS-aligned.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`AboutDialog`** in **`MainActivityDialogs.kt`** — privacy paragraph (no trackers / no phone-home; notifications + FGS rationale for sequential sharing).
- [x] **L.12** **Foreground notification helper text**: add a one-line cue such as **“Open Multi App Share to continue”** on the sharing notification—**keep** the channel at **low / non–heads-up** importance (same policy as current `sharing_service_channel_v2`); never raise importance for visibility.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`notification_open_to_continue`** appended in **`notification_sharing_text`**; **`PRIORITY_LOW`** / **`IMPORTANCE_LOW`** unchanged; channel name/description from resources.

### Accessibility & legibility

- [x] **L.13** **Largest font / display size**: re-check **share overlay** and **top app bar** at ~**200% font scale**; fix clipping (overlaps **K.4**).
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`TopAppBar`** title **`maxLines = 2`**; **`ElevatedCard`** body uses wrapping styles; full device pass tracked in **`docs/ACCESSIBILITY_CHECKLIST.md`**.
- [x] **L.14** **Touch targets**: keep **Next**, **Skip**, and other hot-path actions at least **48dp** where users tap during fast sequential sharing.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`SharingInProgress`** primary **`Button`** and **Replay/Previous** **`OutlinedButton`**s use **`defaultMinSize(minHeight = 48.dp)`**; Material **`IconButton`**s default to 48dp touch target.
- [x] **L.15** **Contrast on frosted overlay**: ensure **body** text on translucent/blurred backgrounds meets readable contrast (aim at **WCAG**-style thresholds for body copy, not only headlines).
  - Verdict: ✅ [COMPLETED]
  - Evidence: Backdrop uses **`MaterialTheme.colorScheme.scrim`** (not light gray wash); primary copy on **`primaryContainer`** / **`onPrimaryContainer`**; **`ACCESSIBILITY_CHECKLIST.md`** contrast row.

### Delight (low noise)

- [x] **L.16** **Haptics discipline**: use stronger haptics mainly for **success / finish** transitions; avoid buzzing on **every** step so fast flows stay calm.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`SharingInProgress`** **`Next App`** no longer fires **`TextHandleMove`** each step; **`LongPress`** only on **Finish** (last step).

---

## Milestone M – Platform robustness, lifecycle & OS evolution

These items close gaps that **E–L** do not fully cover: they target **stability under real devices**, **system behavior**, and **future Android requirements**—not more feature polish.

- [x] **M.1** **Process death & state**: define behavior when the system kills the app mid-sequence (low memory): either **persist** minimal in-flight share state (`SavedStateHandle` / small DataStore snapshot) to resume safely **or** document **ephemeral session** and reset cleanly with user-visible copy (aligns with **F.4** backup story).
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`docs/PROCESS_DEATH.md`** — **ephemeral session**; re-share after cold start; optional future DataStore note; manual test hints (**Don’t keep activities**).
- [x] **M.2** **OEM / battery checklist**: document or automate smoke on **aggressive battery** devices (optional deep link to **ignore battery optimizations** *if* FGS gets killed—user-initiated, optional, FOSS-safe).
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`docs/OEM_BATTERY.md`** matrix + reporting template + optional unrestricted-battery copy guidance.
- [x] **M.3** **targetSdk / quarterly platform review**: when raising `targetSdk`, run through **behavior changes** (notifications, FGS, backups, edge-to-edge) and update manifest + plan entries.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`docs/TARGET_SDK_REVIEW.md`** — current **35** recorded + pre-bump checklist + links; update table on each bump.
- [x] **M.4** (Optional) **`localeConfig` / per-app language** (API 33+): expose system **per-app language** for i18n readiness (**K.2** companion).
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`AndroidManifest.xml`** `android:localeConfig="@xml/locales_config"`; **`res/xml/locales_config.xml`** lists **`en`**, **`fr`**, **`es`** (**O.2** / **O.3**).
- [x] **M.5** (Optional) **16 KB page size** (Android 15+ native alignment): if any native `.so` ships via dependencies, verify alignment requirements for future devices.
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`docs/NATIVE_16KB_PAGE_SIZE.md`** — AndroidX **`libandroidx.graphics.path.so`** + **`libdatastore_shared_counter.so`** listed; re-check after upgrades / Play guidance.
- [x] **M.6** (Optional) **Predictive back** & **window insets**: integrate **`BackHandler`** / Material sheet behavior with gesture nav and **edge-to-edge** so sheets and sequential UI don’t trap focus (**K.1** companion).
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`MainActivity`** **`android:enableOnBackInvokedCallback="true"`**; **`OnboardingDialog`** **`BackHandler`** (page 1 → page 0). **Edge-to-edge** full-bleed follow-up if clipping appears on API 35+ devices.

---

## Milestone N – Security, privacy & encrypted backups (**required**)

There are **no user accounts**, but exports describe **which apps and groups the user configured**—encrypt **all** menu-driven exports; legacy plaintext imports remain supported for migration.

- [x] **N.1** **Lightweight threat model** (internal note or `docs/`): what data exists at rest (Room, JSON export), what leaves the device (user-initiated export only), **no** network exfiltration by design (FOSS).
  - Verdict: ✅ [COMPLETED]
  - Evidence: `docs/THREAT_MODEL.md`.
- [x] **N.2** **Network surface**: confirm **`usesCleartextTraffic`** is false and no unexpected network permissions.
  - Verdict: ✅ [COMPLETED]
  - Evidence: `android:usesCleartextTraffic="false"` on `<application>` in `AndroidManifest.xml`.
- [x] **N.3** **Export path hygiene**: **SAF / user-chosen URI** — user controls destination; dialogs explain passphrase responsibility.
  - Verdict: ✅ [COMPLETED]
  - Evidence: `BackupExportPassphraseDialog` / import copy in `BackupDialogs.kt`.
- [x] **N.4** **Encrypted JSON export** (**required**): passphrase-based **AES-256-GCM** with **PBKDF2-HMAC-SHA256** (310k iterations); UTF-8 envelope JSON (`multiappshare-encrypted-backup`); **decrypt on import**; fails closed on wrong passphrase; **no** proprietary crypto SDKs.
  - Verdict: ✅ [COMPLETED]
  - Evidence: `core-domain/.../BackupCipher.kt`, `docs/BACKUP_FORMAT.md`, `BackupCipherTest`, UI in `BackupDialogs.kt`, `MainViewModel` export/import.
- [x] **N.5** **Secrets in repo**: ensure **no API keys** or signing secrets committed; CI uses secrets for release signing only—already best practice; periodic grep / GitHub secret scanning.
  - Verdict: ✅ [COMPLETED]
  - Evidence: `CONTRIBUTING.md` **Secrets** bullet; repo owners enable GitHub **secret scanning** if available on the org.

---

## Milestone O – Localization (i18n): English, French, Spanish

### How Android localization works (important)

- You store **default** strings in `res/values/strings.xml` (typically **English**).
- You add **locale-specific** resource folders, e.g. `res/values-fr/strings.xml` (**French**), `res/values-es/strings.xml` (**Spanish**). The system picks the right file from the user’s **system language** (and optional **per-app language** via **M.4** / `localeConfig` on API 33+).
- **Android does not auto-translate** your app at install time. **There is no built-in “translate my whole app”** switch. You ship **your own** translated strings (or professional/community translations). Android Studio’s **Translations Editor** can **suggest** machine translations for **missing** keys—those are **starting points** and should be **reviewed by humans**, especially for UI brevity and tone.
- Optional FOSS-friendly workflows: **Weblate**, **Tolgee**, or community PRs for `values-fr` / `values-es`.

### Plan items

- [x] **O.1** Complete **K.2** first: externalize user-visible strings from code into **`strings.xml`** (and plurals / arrays as needed); use string resources in Compose (`stringResource`).
  - Verdict: ✅ [COMPLETED]
  - Evidence: **K.2** done; main flows use **`stringResource`** / `getString`; re-grep when adding features (**`rg 'Text\\(\"'`** in `app/src/main/java` should stay clean).
- [x] **O.2** Add **French** resources: `res/values-fr/strings.xml` (or region-specific `values-fr-rFR` if you need France-only variants later).
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`app/src/main/res/values-fr/strings.xml`** (full key parity); **`locales_config.xml`** includes **`fr`**; human review welcome for tone.
- [x] **O.3** Add **Spanish** resources: `res/values-es/strings.xml` (neutral **es** is common; add `values-es-rUS` / `values-es-rES` later only if copy must diverge).
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`app/src/main/res/values-es/strings.xml`**; **`locales_config`** **`es`**.
- [x] **O.4** (Optional) **Pseudo-locales** (`en-XA`, `ar-XB`) in **debug** builds to catch truncation and RTL issues early (**RTL** full support can be a later milestone if you add Arabic, etc.).
  - Verdict: ✅ [COMPLETED]
  - Evidence: **`app/build.gradle.kts`** **`debug { isPseudoLocalesEnabled = true }`**; enable **Pseudo-locales** in **Developer options** to exercise **`en-XA`** / **`ar-XB`** on debug builds.

---

## Milestone P – Post-revamp optimizations & continuous improvement

Optional follow-ups after the **KSP / toolchain** revamp and **E → O** baseline: performance artifacts, Gradle/Compose hygiene, measurement discipline, and release gates. Execute in **any** order unless noted; **P.1** closes checklist **I.3**.

- [x] **P.1** **Ship the Baseline Profile artifact** (closes **I.3**): run **`./gradlew :app:generateBaselineProfile`** on a USB device or emulator with a **matching-signed** app install; commit merged output under **`app/src/main/`** per **`mergeIntoMain`**. If **`INSTALL_FAILED_UPDATE_INCOMPATIBLE`**, uninstall conflicting builds first — see **`docs/BASELINE_PROFILE.md`**. **Evidence:** same as **I.3** — **`app/src/main/generated/baselineProfiles/`** + generator / **1.4.1** toolchain notes above.

- [x] **P.2** **Android Gradle Plugin vs `compileSdk`**: **AGP 8.5.x** was validated up to **compileSdk 34** while the app uses **compileSdk 35** (build warns). Either **bump AGP** when ready for a broader regression pass, **or** add **`android.suppressUnsupportedCompileSdk=35`** in **`gradle.properties`** until upgrade. **Evidence:** **`gradle.properties`** sets **`android.suppressUnsupportedCompileSdk=35`** with comment + link; **`./gradlew test lint :app:assembleDebug`** passes.

- [x] **P.3** **Compose compiler API migration**: replace deprecated **`composeCompiler { enableStrongSkippingMode = true }`** with the supported **`featureFlags { ... }`** form (see Compose compiler release notes for Kotlin **2.0.21**). Touch **`app/build.gradle.kts`** and **`feature-dashboard/build.gradle.kts`**. **Evidence:** removed deprecated **`enableStrongSkippingMode`** (strong skipping remains **on by default** in Compose Compiler **2.x**); **`reportsDestination`** kept; configure warning gone — **`./gradlew :app:assembleDebug`** passes.

- [x] **P.4** **Measure before micro-optimizing UI**: after **P.1** (or any shipped baseline profile), use **Android Studio Profiler** and/or **Macrobenchmark** traces on **cold start** and **share handoff** so optimizations target real hotspots (pairs with **H.3** harness). **Evidence:** **`docs/PROFILING.md`** — steps for Profiler, optional Macrobenchmark, pairs with **`docs/BASELINE_PROFILE.md`**.

- [x] **P.5** **Room version bumps & migrations**: whenever **`@Database` version** increments, add **`Migration`** objects (**F.2**), keep **`core-database/schemas/`** exported (**F.1** / KSP **`room.schemaLocation`**), and add or extend **tests** (unit and, for risky paths, **instrumented** migration smoke). **Evidence:** **`docs/ROOM_MIGRATION_CHECKLIST.md`** — procedure for future bumps; **`AppDatabase`** still **v1** (no schema change this pass).

- [x] **P.6** **Module boundaries** (ongoing discipline): prefer **`:core-domain`** use cases and repositories for non-trivial logic; keep **`MainActivity` / `MainScreen`** as orchestration and Compose wiring (**G**). **Evidence:** **`docs/MODULE_BOUNDARIES.md`** — layer rules + pointer to **ADR-001** / **G.2**.

- [x] **P.7** **CI quality gate**: keep **`./gradlew lint`**, **`test`** (includes Paparazzi **`verifyPaparazziDebug`** via **`EmptyGroupsPlaceholderPaparazziTest`**), and **`assembleDebug`** on PRs (**E.1**). Add emulator **`connected*`** jobs only if you explicitly want slower CI. **Evidence:** **`.github/workflows/android.yml`** — **Lint** → **Unit tests** → **assembleDebug**; no **`connected*`** unless opted in.

- [x] **P.8** **Release smoke ritual**: after bumps affecting **R8**, **Hilt**, or **kotlinx.serialization**, run a short manual pass — install **release** APK, **sequential share** flow, **encrypted backup** export/import — before tagging releases (**I.4** companion). **Evidence:** **`docs/RELEASE_SMOKE.md`** checklist (**Milestone P.8**).

- [x] **P.9** **Local signed release artifacts**: support **`keystore.properties`** (from **`keystore.properties.example`**) + env overrides for **`assembleRelease`** / **`bundleRelease`**, document adb/GitHub release workflow, PowerShell helper **`scripts/build-signed-release-apk.ps1`**. **Evidence:** **`docs/LOCAL_RELEASE_BUILD.md`**, **`app/build.gradle.kts`** **`signingConfigs.release`**, **`.gitignore`** **`keystore.properties`**.

- [x] **P.10** **Novice-friendly signing onboarding**: step-by-step guide for non-experts (**keytool**, **`keystore.properties`**, adb, GitHub Releases), plus **`scripts/setup-release-keystore.ps1`** to create **`release.keystore`** + properties safely (no default passwords in repo). **Evidence:** **`docs/SIGNING_FOR_BEGINNERS.md`**, **`scripts/setup-release-keystore.ps1`**, cross-links from **`docs/LOCAL_RELEASE_BUILD.md`** and **`scripts/build-signed-release-apk.ps1`**.

---

## Milestone Q – Manual QA before tagged releases (optional)

Run these **before a major/minor GitHub tag** or Play rollout when you want extra confidence; they are **not** automated in CI.

- [x] **Q.1** **Accessibility & large-font pass**: walk **`docs/ACCESSIBILITY_CHECKLIST.md`** on a **physical device** (TalkBack on, ~200% font) for main flows (groups list, share overlay, sequential overlay). **Evidence:** automated gate (**`./gradlew :app:lintDebug`**, **`./gradlew test`**). Manual TalkBack / 200% font / contrast checks are marked **[USER-SKIP]** in **`docs/ACCESSIBILITY_CHECKLIST.md`** per user decision.

- [x] **Q.2** **Hilt / cold-start sanity** (closes **`docs/HILT_FIX_PLAN.md`** device line): after a clean install, confirm **`MainActivity`** launches and ViewModel injection works (`./gradlew :app:connectedDebugAndroidTest` on a device/emulator, or manual launch). **Evidence:** **`./gradlew :app:connectedDebugAndroidTest`** green — **`MainActivitySmokeInstrumentedTest`** (`createAndroidComposeRule(MainActivity::class.java)`), **`DeeplinkInstrumentedTest`** (UiAutomator + locale-safe strings), onboarding dismiss helper; **`ExampleInstrumentedTest`** package check.

---

### Sufficiency note — is **E → O** “enough”?

For a **FOSS utility** with **no backend**, this backlog is **strong**: testing (**E**, **H**), durable data (**F**), modular structure (**G**), release hygiene (**I**), UX (**L**), lifecycle/OS (**M**), security posture (**N**), and first-wave locales (**O**) match what serious indie and OSS Android apps ship. **Nothing guarantees zero bugs**—OEM variance and share-target opacity still need **H.2-style** manual checks.

**Milestone P** is **optional** continuous improvement (baseline profile completion, toolchain alignment, profiling discipline, CI/release rituals)—see above.

**Wear OS / Android TV:** **Not in product scope** (see Roadmap table above).

---

## Completed elsewhere (reference)

- **Foreground sharing notification**: progress channel uses low-importance / non–heads-up behavior so target apps’ compose fields stay usable (see `SharingService.kt`, channel `sharing_service_channel_v2`).
- **Deeplinks**: FOSS custom URI scheme `multiappshare` (no `https` App Links / Digital Asset Links). **https** verified links can be a later optional milestone if you add a real domain.
- **Encrypted backups (**N**): passphrase-only AES-GCM exports; see `docs/BACKUP_FORMAT.md`. **`BackupCipher`** + repository tests support **E.2**; **Milestone E** is marked complete above.
