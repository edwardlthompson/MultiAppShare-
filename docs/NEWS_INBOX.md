# News Inbox

Auto-collected inbox of news, upstream updates, security fixes, and ideas relevant to this repo, appended daily by News Bot.
A Cursor planning agent should review these items and fold the useful ones into BUILD_PLAN; entries are append-only, newest at the bottom.


## 2026-11-13

**News and updates**

- Android 17 QPR2 stable: lock-screen widget editing, status-bar notification filters, share-sheet changes, brightness override. [source](https://www.androidauthority.com/android-17-qpr2-stable-3733645/)
- Kotlin 2.5.0-RC released (fixes a reified Enum<T> inference regression); Gradle 9.9.1 / 8.14.7 patch releases. [source](https://github.com/JetBrains/kotlin/releases/tag/v2.5.0-RC)

**Tasks for the planning agent**

- [x] Verify share flow against Android 17 QPR2 share-sheet changes — The share sheet is the app's core surface. Label: `[ADB]` · Priority: P2 · https://www.androidauthority.com/android-17-qpr2-stable-3733645/ (synced to BUILD_PLAN)
- [ ] Smoke-test Kotlin 2.5.0-RC and bump Gradle wrapper — Catches compiler regressions before stable. Label: `[AGENT]` · Priority: P3 · https://github.com/JetBrains/kotlin/releases/tag/v2.5.0-RC

## 2026-11-16

**News and updates**

- GitHub Actions will block `pull_request_target` runs in public repos by default from Dec 2 unless an Actions event policy allows them; today (Nov 16) 12:00–14:00 AST is the second brownout. [source](https://github.blog/changelog/2026-11-02-pull_request_target-blocked-by-default-december-2/) Your `.github/workflows/pr-title.yml` uses it and only needs `pull-requests: read`.

**Tasks for the planning agent**

- [x] Switch pr-title.yml from pull_request_target to pull_request — The semantic PR-title check will fail on Dec 2 (and during brownouts), and plain pull_request works for same-repo PRs. Label: `[AGENT]` · Priority: P1 · https://github.blog/changelog/2026-11-02-pull_request_target-blocked-by-default-december-2/ (synced to BUILD_PLAN)

## 2026-11-18

Catch-up for Nov 17–18 (the daily brief did not run those mornings).

**News and updates**

- Both `pull_request_target` brownouts (Nov 9 and Nov 16) have passed, and `.github/workflows/pr-title.yml` is still on `pull_request_target`. The existing P1 to switch it to `pull_request` remains open; the default block becomes permanent on Dec 2. [source](https://github.blog/changelog/2026-11-02-pull_request_target-blocked-by-default-december-2/)

**Tasks for the planning agent**

_No new tasks. See the news items above._

## 2026-11-19

**News and updates**

- Jetpack Compose November '26 release is stable: Compose BOM 2026.11.01 (Compose 1.13) with Material3 1.5.0. The BOM needs Kotlin 2.2+ and compileSdk 37.1. Material3 1.5.0 breaks Slider/RangeSlider source (stateless overloads deprecated, `onValueChange` now required, RangeSliderState activeRangeStart/End renamed to startValue/endValue); SecureTextField now defaults to system obfuscation, and clickables play platform click sounds by default. [source](https://developer.android.com/blog/posts/jetpack-compose-november-2026-release) · [Material3 notes](https://developer.android.com/jetpack/androidx/releases/compose-material3)
- Material3 1.5.0 adds a `material3-ripple` artifact that draws keyboard/D-pad focus as inset focus rings, and Compose 1.13 clickables now play the system click sound by default (respecting system settings). [source](https://developer.android.com/jetpack/androidx/releases/compose-material3)

**Tasks for the planning agent**

- [x] Bump Compose BOM 2026.08.00 to 2026.11.01 (Compose 1.13 / Material3 1.5.0) and fix Slider/RangeSlider call sites — Material3 1.5.0 makes `onValueChange` required on Slider/RangeSlider (no Slider/RangeSlider calls found, but check other deprecations) and the BOM requires Kotlin 2.2+ and compileSdk 37.1, so a Dependabot/Argus BOM bump will fail unless AGP accepts 37.1 and the call sites are fixed first. Label: `[AGENT]` · Priority: P2 · https://developer.android.com/blog/posts/jetpack-compose-november-2026-release (synced to BUILD_PLAN)
- [ ] After the BOM 2026.11.01 bump, check keyboard/D-pad focus rings on the main flows and remove manual click sounds that would now double-fire — Inset focus rings make focus visible for keyboard and switch-access users, and stray double UI sounds are noticeable in an accessibility-focused app. Label: `[AGENT]` · Priority: P3 · https://developer.android.com/jetpack/androidx/releases/compose-material3
