# Prompt library — Multi App Share

Effective prompt patterns for this repository.

## Room migration

```
Follow docs/ROOM_MIGRATION_CHECKLIST.md. Bump @Database version, add Migration objects,
export schema to core-database/schemas/, add unit test for migration path.
Release builds must NOT use fallbackToDestructiveMigration().
```

## Paparazzi golden refresh

```
UI changed intentionally in [Composable name]. Run ./gradlew :app:recordPaparazziDebug,
commit updated files under app/src/test/snapshots/images/. See docs/PAPARAZZI.md.
```

## F-Droid version bump

```
Bump versionCode and versionName in app/build.gradle.kts, metadata/com.edwardlthompson.multiappshare.yml,
fastlane/metadata/android/en-US/changelogs/[versionCode].txt, CHANGELOG.md.
Follow FDROID_MAINTENANCE.md. Do not commit signing secrets.
```

## Encrypted backup feature template

```
Follow docs/GOLDEN_PATH.md: pure logic in :core-domain, repository boundary, ViewModel orchestration,
Compose dialog, unit test with in-memory/fakes. No proprietary crypto SDKs.
```

## Share flow regression

```
After sharing UX changes: ./gradlew test, docs/MANUAL_SHARE_CHECKLIST.md on device,
verify SharingService low-importance notification unchanged. Read docs/RETURN_PATH.md.
```

## Agent milestone execution

```
Read AGENTS.md and docs/BUILD_PLAN.md. Use Plan Mode for non-trivial items.
Respect executor tags: [AGENT] vs [ADB] vs [HUMAN]. Parallel tasks must not overlap files.
Include Critique subsection in plan. Run /verify before marking complete.
```

## Bootstrap slash commands

```
/verify   — docs + validate-bootstrap + feature-gate (+ CI poll via /ci)
/gates    — local Gradle + bootstrap checks only
/prerelease — PRE_RELEASE_AUDIT + Paparazzi
/ship     — prerelease + push + regress (explicit push approval)
/debug    — defect investigation (not /audit)
```

See [`docs/help/BATCH_COMMANDS.md`](docs/help/BATCH_COMMANDS.md) for full catalog.
