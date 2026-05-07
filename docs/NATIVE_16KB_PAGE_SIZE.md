# 16 KB page size & native libraries (Milestone **M.5**)

Android 15+ devices may use **16 KB memory pages**. Native **`.so`** binaries must meet alignment rules imposed by the toolchain and dependencies.

## What this app ships (verified)

A **debug** APK listing (`jar tf …apk | findstr "\.so"`) showed:

| Library | ABIs |
|--------|------|
| `libandroidx.graphics.path.so` | arm64-v8a, armeabi-v7a, x86, x86_64 |
| `libdatastore_shared_counter.so` | same |

These come from **AndroidX** (graphics path, DataStore), not from app-authored JNI.

## Actions

1. **Before raising `targetSdk` or shipping to new form factors** — Re-run the APK/AAB inspection after dependency upgrades; check [16 KB page size](https://developer.android.com/guide/practices/page-sizes) for the current NDK/AGP guidance.
2. **Release builds** — Prefer verifying **`bundletool` / Play pre-launch** reports for alignment warnings once you publish to a track.
3. **If you add NDK code** — Build with a NDK/CMake revision that emits **16 KB–eligible** ELF segments; add CI or a local script that fails on bad alignment.

## Reproduce locally

```bash
./gradlew :app:assembleDebug
jar tf app/build/outputs/apk/debug/MultiAppShare-v*-debug.apk | grep '\.so'
```

Replace with `assembleRelease` / AAB path for store-like artifacts.
