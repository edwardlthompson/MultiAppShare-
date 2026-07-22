# Android module — Multi App Share

Maps bootstrap `modules/android/MODULE.md` (agent-project-bootstrap **v0.15.0**) to this production repo.

**Do not use** template `examples/android/` — production code lives at the repo root.

## Production modules

| Module | Role |
|--------|------|
| `:app` | Application, MainActivity, share flow, Hilt entry |
| `:feature-dashboard` | Dashboard/history/about Compose screens |
| `:core-domain` | Use cases, repository interfaces (pure Kotlin) |
| `:core-database` | Room, repository implementations |
| `:core-ui` | Shared XML themes/resources |
| `:baselineprofile` | Baseline profile generator |

See [`docs/MODULE_BOUNDARIES.md`](../../docs/MODULE_BOUNDARIES.md).

## Template mapping

| Template concept | This repo |
|------------------|-----------|
| `examples/android/` | Root Gradle modules above |
| Golden Path feature | Encrypted backup — [`docs/GOLDEN_PATH.md`](../../docs/GOLDEN_PATH.md) |
| Path map | [`docs/BOOTSTRAP_TEMPLATE_MAP.md`](../../docs/BOOTSTRAP_TEMPLATE_MAP.md) |
| Alignment record | [`docs/BOOTSTRAP_ALIGNMENT.md`](../../docs/BOOTSTRAP_ALIGNMENT.md) |

## Gates

```bash
./gradlew lint test detekt koverXmlReport assembleDebug
bash scripts/feature-gate.sh --stack android
bash scripts/validate-bootstrap.sh --quick
```

Primary CI: `.github/workflows/android.yml` (**Android CI**).

## Distribution

- GitHub Releases + F-Droid via [`metadata/`](../../metadata/) and [`FDROID_MAINTENANCE.md`](../../FDROID_MAINTENANCE.md)
- Reproducible builds: [`docs/REPRODUCIBLE_BUILDS.md`](../../docs/REPRODUCIBLE_BUILDS.md)

## Out of scope

Wear OS, Android TV, GMS/Firebase, backend accounts, web/python/node stacks.
