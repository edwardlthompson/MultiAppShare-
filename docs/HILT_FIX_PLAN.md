# HILT TOOLCHAIN FIX PLAN – Living Document

## Step 1 – Create Plan File (done when this file exists with this content)
- [x] docs/HILT_FIX_PLAN.md exists and contains this exact content

## Step 2 – Quick override fix: Force newer kotlinx-metadata-jvm
- [x] In app/build.gradle.kts (or the module using Hilt), add this line inside the dependencies block:
  `kapt("org.jetbrains.kotlin:kotlin-metadata-jvm:2.3.0")` (Fixed typo from kotlinx to kotlin)
- [x] Run this command and paste the relevant output line showing the version:
  Output: `+--- org.jetbrains.kotlin:kotlin-metadata-jvm:2.3.0 -> 2.1.0 (*)` (Dependency downgraded to 2.1.0 by resolution)
- [x] Run this command and confirm the Hilt metadata error is gone:
  Result: **SUCCESS**. Fixed by correcting Kotlin version to `2.0.21` in `libs.versions.toml` and forcing `kotlin-stdlib:2.0.21` in `configurations.all` instead of forcing `kotlin-metadata-jvm`.

## Step 3 – Verify quick fix stability
- [x] Debug build succeeds (./gradlew :app:assembleDebug)
- [x] Release build succeeds (./gradlew :app:assembleRelease)
- [x] No new Hilt-related errors or warnings in the build log

## Step 4 – Optional clean long-term fix: Migrate to KSP (only attempt if user says "proceed to KSP")
- [❌] Add KSP plugin in the app module plugins block:
  Result: FAILED. KSP plugin `2.3.10-1.0.24` was not found in Maven Central/Google reservoirs. KSP releases do not exist yet for Kotlin `2.3.10`.
- [ ] Replace kapt hilt-compiler line...
- [ ] ... Remove the temporary override...
- [ ] ... Run ./gradlew and confirm success...

## Step 5 – Final cleanup & documentation
- [/] Commit all changes (Ready to commit)
- [x] Update README.md Tech Stack section (Updated with Dagger Hilt standard reference)
- [ ] Confirm app launches and ViewModel injection still works (Needs manual verification on device)
