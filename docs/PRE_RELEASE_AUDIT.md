# Pre-release audit

Run before every **`v*`** tag or production release.

## Automated (agent / CI)

| Step | Command / artifact |
|------|-------------------|
| Lint | `./gradlew lint` |
| Unit tests + Paparazzi | `./gradlew test` |
| Static analysis | `./gradlew detekt` |
| Coverage report | `./gradlew koverXmlReport` |
| Debug build | `./gradlew assembleDebug` |
| CodeQL | Green on `main` (GitHub Actions) |
| Dependabot | No critical unmerged security PRs |
| APK size | CI budget check (debug APK ≤ 27 MB) |
| File size limits | `scripts/check-file-limits.ps1` |

## Metadata sync (agent)

- [x] `versionCode` / `versionName` in `app/build.gradle.kts` — **180** / **1.9.4**
- [x] `metadata/com.edwardlthompson.multiappshare.yml`
- [x] `fastlane/metadata/android/en-US/changelogs/180.txt`
- [x] [`CHANGELOG.md`](../CHANGELOG.md) (Keep a Changelog)

## Device smoke (`[ADB]` + human)

Follow [`RELEASE_SMOKE.md`](RELEASE_SMOKE.md):

1. Install signed **release** APK (minified; **S.9.2** / **R.5.1**)
2. Cold launch
3. Sequential share handoff (include rotation + failed-target cases per Milestone **S**)
4. Encrypted backup export/import
5. Deeplink group expand on cold start (S.6.1)

## Accessibility (`[Human]`)

Follow [`ACCESSIBILITY_CHECKLIST.md`](ACCESSIBILITY_CHECKLIST.md) on a physical device (TalkBack, ~200% font) when UX changed.

## Release (`[Human]`)

- [ ] Push tag `vX.Y.Z`
- [ ] Verify GitHub Release APK upload (`android.yml`)
- [ ] F-Droid metadata PR if applicable ([`FDROID_MAINTENANCE.md`](../FDROID_MAINTENANCE.md))

Only when all applicable rows pass: ship the release.
