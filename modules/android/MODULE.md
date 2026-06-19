# Android module — Multi App Share

Maps bootstrap `modules/android/MODULE.md` to this production repo.

## Production modules

| Module | Role |
|--------|------|
| `:app` | Application, MainActivity, share flow, Hilt entry |
| `:feature-dashboard` | Dashboard/history/about Compose screens |
| `:core-domain` | Use cases, repository interfaces (pure Kotlin) |
| `:core-database` | Room, repository implementations |
| `:core-ui` | Shared XML themes/resources |
| `:baselineprofile` | Baseline profile generator |

## Template mapping

- Template `examples/android/` → **this repo root** (see [`docs/BOOTSTRAP_TEMPLATE_MAP.md`](../docs/BOOTSTRAP_TEMPLATE_MAP.md))
- Golden Path feature: encrypted backup — [`docs/GOLDEN_PATH.md`](../docs/GOLDEN_PATH.md)

## Gates

```bash
./gradlew lint test detekt koverXmlReport assembleDebug
bash scripts/feature-gate.sh --stack android
```

## Distribution

- GitHub Releases + F-Droid via [`metadata/`](../metadata/) and [`FDROID_MAINTENANCE.md`](../FDROID_MAINTENANCE.md)
- Reproducible builds: [`docs/REPRODUCIBLE_BUILDS.md`](../docs/REPRODUCIBLE_BUILDS.md)

## Out of scope

Wear OS, Android TV, GMS/Firebase, backend accounts.
