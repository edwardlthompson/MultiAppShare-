# Baseline profile (Milestone **I.3**)

Merged baseline artifacts are checked in under **`app/src/main/generated/baselineProfiles/`** (`baseline-prof.txt`, `startup-prof.txt`), produced by **`./gradlew :app:generateBaselineProfile`** with **`mergeIntoMain = true`** in **`app/build.gradle.kts`**. Regenerate after materially changing cold start / first-frame paths.

## Harness (**H.3** — **`:baselineprofile`** module)

The baseline profile module is already in the repo. To recreate from scratch or align versions:

1. In Android Studio: **File → New → New Module → Baseline Profile** (targets **:app**), *or* follow [Create Baseline Profiles](https://developer.android.com/topic/performance/baselineprofiles/create-baselineprofile).
2. Root **`build.gradle.kts`**: declare `androidx.baselineprofile` and `com.android.test` plugins (`apply false`) aligned with your AGP (`gradle/libs.versions.toml`).
3. App module: `baselineProfile(project(":baselineprofile"))` (or the name Studio generates) and optional `baselineProfile { saveInProject = true }` in `android { }`.
4. Connect a **device or emulator (API 28+)** and run:

   ```bash
   ./gradlew :app:generateBaselineProfile
   ```

   With **`mergeIntoMain = true`** in **`app/build.gradle.kts`**, the merged profile is copied into the app module for R8 (see also **`copyBaselineProfileIntoSrc`** / **`mergeBaselineProfile`** tasks).

5. Commit the generated baseline artifact(s) under **`app/src/main/`** (layout depends on AGP/plugin version), then re-run **`./gradlew :app:assembleRelease`** and smoke startup.

## Repo state

- **`:baselineprofile`** module: **`BaselineProfileGenerator`** (startup / main frame via **`pressHome()`** + **`startActivityAndWait()`**).
- **`:baselineprofile`** also depends on **`junit`**, **`androidx.test.ext:junit`**, and **`androidx.test:runner`** so **`@RunWith(AndroidJUnit4::class)`** and **`@LargeTest`** resolve.
- **App** applies **`androidx.baselineprofile`** and depends on **`baselineProfile(project(":baselineprofile"))`**.
- The **profile artifact** is committed after a successful **`generateBaselineProfile`** run with a **connected device/emulator (API 28+)**; generator uses an explicit **`MAIN`/`LAUNCHER`** **`Intent`** with **`ComponentName`** for **`com.multiappshare.MainActivity`** (works better on some OEM ROMs than implicit resolver).

### Troubleshooting installs

If Gradle reports **`INSTALL_FAILED_UPDATE_INCOMPATIBLE`** / signature mismatch, the device has a build signed with a **different key** (e.g. Play Store) than the **nonMinifiedRelease** test APK. **Uninstall** the existing app from that user profile (Settings → Apps, or **`adb uninstall com.edwardlthompson.multiappshare`** when the OS allows), then re-run **`./gradlew :app:generateBaselineProfile`**.

### Macrobenchmark: “Unable to confirm activity launch completion”

If **`BaselineProfileGenerator`** fails with **`IllegalStateException`** from **`MacrobenchmarkScope.amStartAndWait`** / **`dumpsys gfxinfo`** empty frames:

- Align **`adb`** with the device: use the **Android SDK platform-tools** `adb` (Gradle may spawn a different daemon version—restart **`adb kill-server`** / IDE Device Explorer).
- Retry on an **emulator** (API **33–34**) if an OEM device blocks **`gfxinfo`** framestats.
- Ensure **`packageName`** in **`BaselineProfileGenerator`** matches **`applicationId`** (`com.edwardlthompson.multiappshare`).
- Capture **`adb shell dumpsys gfxinfo com.edwardlthompson.multiappshare reset`** after manual launch for vendor-specific quirks.

See [Macrobenchmark troubleshooting](https://developer.android.com/studio/profile/macrobenchmark#getting_started).

### After generation

Commit the merged **`baseline-prof`** / startup profile output under **`app/src/main/`** as emitted by AGP (see **`copyBaselineProfileIntoSrc`** / **`mergeBaselineProfile`** tasks).
