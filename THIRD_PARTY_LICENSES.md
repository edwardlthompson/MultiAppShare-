# Third-party licenses — Multi App Share

This app is **MIT licensed** ([`LICENSE`](../LICENSE)). Dependencies are declared in Gradle:

- Version catalog: [`gradle/libs.versions.toml`](../gradle/libs.versions.toml)
- Module `build.gradle.kts` files

## Major OSS dependencies (non-exhaustive)

| Library | License | Use |
|---------|---------|-----|
| Kotlin / Jetpack Compose / AndroidX | Apache 2.0 | UI, lifecycle, Room, DataStore |
| Dagger Hilt | Apache 2.0 | DI |
| Coil | Apache 2.0 | Image loading |
| Kotlinx Serialization | Apache 2.0 | JSON backup envelope |
| Timber | Apache 2.0 | Debug logging |
| LeakCanary | Apache 2.0 | Debug-only leak detection |

Generate a full dependency report locally:

```bash
./gradlew :app:dependencies --configuration releaseRuntimeClasspath
```

For F-Droid and store listings: **no analytics, no proprietary SDKs, no network exfiltration** — see [`docs/PRIVACY.md`](docs/PRIVACY.md) and [`docs/THREAT_MODEL.md`](docs/THREAT_MODEL.md).
