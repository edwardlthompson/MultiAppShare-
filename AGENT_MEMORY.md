# Agent memory index — Multi App Share

Central index for persistent context. Append at milestone boundaries only.

## Project dimensions

| Dimension | Value |
|-----------|-------|
| **Platform** | Native Android — Kotlin 2.4.0, Jetpack Compose, Hilt 2.59, Room 2.8, Gradle 9.5 / AGP 9.2 |
| **Purpose** | Sequential multi-app sharing via custom groups; no backend, no accounts |
| **Distribution** | MIT; [GitHub Releases](https://github.com/edwardlthompson/MultiAppShare-/releases); [F-Droid](https://f-droid.org/packages/com.edwardlthompson.multiappshare/) via [`metadata/com.edwardlthompson.multiappshare.yml`](metadata/com.edwardlthompson.multiappshare.yml) |
| **App ID** | `com.edwardlthompson.multiappshare` |
| **Out of scope** | Wear OS, Android TV, web client, GMS/Firebase |

## Module map

`:core-domain` → `:core-database`, `:core-ui` → `:feature-dashboard` → `:app`

See [`docs/MODULE_BOUNDARIES.md`](docs/MODULE_BOUNDARIES.md).

## Security and privacy

- [`docs/THREAT_MODEL.md`](docs/THREAT_MODEL.md) — data at rest, export-only egress, no network exfiltration
- [`docs/BACKUP_FORMAT.md`](docs/BACKUP_FORMAT.md) — AES-256-GCM encrypted JSON exports
- [`docs/BACKUP_AND_CLOUD.md`](docs/BACKUP_AND_CLOUD.md) — Auto Backup vs manual export

## Platform compliance

- [`docs/TARGET_SDK_REVIEW.md`](docs/TARGET_SDK_REVIEW.md) — `compileSdk` **37**, `targetSdk` **35**
- [`docs/NATIVE_16KB_PAGE_SIZE.md`](docs/NATIVE_16KB_PAGE_SIZE.md) — native `.so` alignment
- [`docs/REPRODUCIBLE_BUILDS.md`](docs/REPRODUCIBLE_BUILDS.md) — SOURCE_DATE_EPOCH, F-Droid parity

## Build and release

- CI: [`.github/workflows/android.yml`](.github/workflows/android.yml)
- Local release: [`docs/LOCAL_RELEASE_BUILD.md`](docs/LOCAL_RELEASE_BUILD.md)
- F-Droid playbook: [`FDROID_MAINTENANCE.md`](FDROID_MAINTENANCE.md)

## Living plans

- Active roadmap: [`docs/BUILD_PLAN.md`](docs/BUILD_PLAN.md) — pre-release gate; next milestone TBD
- Completed milestones A–T: [`docs/COMPLETED_TASKS.md`](docs/COMPLETED_TASKS.md)
- ADRs: [`docs/adr/README.md`](docs/adr/README.md)
- Decision log: [`docs/DECISION_LOG.md`](docs/DECISION_LOG.md)

## Retrospectives

_(Append milestone notes here at boundaries.)_
