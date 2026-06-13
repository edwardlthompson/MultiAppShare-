# Reproducible builds

Multi App Share targets F-Droid reproducible build verification where possible.

## Toolchain pinning

- **Gradle wrapper**: commit `gradle/wrapper/gradle-wrapper.properties` + JAR; verify checksum when upgrading.
- **Dependencies**: [`gradle/libs.versions.toml`](../gradle/libs.versions.toml) version catalog.
- **JDK**: **21** (Temurin) in CI, devcontainer, and local release docs.

## SOURCE_DATE_EPOCH

Tagged release builds set a fixed epoch so embedded timestamps are deterministic:

```bash
export SOURCE_DATE_EPOCH=1704067200  # 2024-01-01T00:00:00Z
```

Configured in [`.github/workflows/android.yml`](../.github/workflows/android.yml) `release-apk` job.

Local signed release (PowerShell):

```powershell
$env:SOURCE_DATE_EPOCH = "1704067200"
./gradlew :app:assembleRelease
```

## F-Droid recipe

Canonical metadata: [`metadata/com.edwardlthompson.multiappshare.yml`](../metadata/com.edwardlthompson.multiappshare.yml).

Maintenance: [`FDROID_MAINTENANCE.md`](../FDROID_MAINTENANCE.md).

## Verification limits

- **Signing** injects non-reproducible bytes — compare **unsigned** artifacts or F-Droid's detached signature workflow.
- Native `.so` from dependencies may differ by NDK patch level — re-check after AndroidX upgrades ([`NATIVE_16KB_PAGE_SIZE.md`](NATIVE_16KB_PAGE_SIZE.md)).
- PNG assets: strip metadata with [`app/strip_all_pngs.py`](../app/strip_all_pngs.py) before release.

## Optional CI smoke (R.3.2)

On tag builds, CI may build twice with the same `SOURCE_DATE_EPOCH` and compare SHA-256 of unsigned outputs — aspirational; signing may block byte-identical APKs.
