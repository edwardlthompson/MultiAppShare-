# F-Droid Maintenance & Release Guide

This guide keeps Multi App Share aligned with the F-Droid buildserver. Previous upstream attempt: closed MR !34700 (opened from protected `master`, missing Fastlane screenshots, Summary/Description in YAML). Do not repeat those mistakes.

## Current structure

### 1. Metadata location

- **App repo**: `metadata/com.edwardlthompson.multiappshare.yml` is the canonical recipe for the GitLab `fdroiddata` fork. Keep **`Categories`** alphabetically (`Multimedia` then `System`). Include **`AutoName: Multi App Share`**. **Do not** add `Summary` or `Description` — Fastlane at the repo root supplies store text.
- **Fastlane (scraped by F-Droid from the tagged commit)**: `fastlane/metadata/android/en-US/` with `title.txt`, `short_description.txt`, `full_description.txt`, `changelogs/<versionCode>.txt`, `images/icon.png`, `images/featureGraphic.png`, and `images/phoneScreenshots/*.png`.
- **F-Droid data repo**: Edward's fork branch `fdroid/com.edwardlthompson.multiappshare` (not `master`). Open the MR from that branch to [fdroid/fdroiddata](https://gitlab.com/fdroid/fdroiddata) `master`. Merge (do not rebase) upstream `master` into the fork branch first.

### 2. Version alignment (CRITICAL)

Before every release, these must match **exactly**:

1. `app/build.gradle.kts` → quoted `versionName = "1.10.1"` (F-Droid `checkupdates` regex) and matching `val appVersion` / `versionCode` **183**
2. `metadata/com.edwardlthompson.multiappshare.yml` → `CurrentVersion` / `CurrentVersionCode` and the `Builds:` `commit:` (use `CurrentVersion`, not `CurrentVersionName`)
3. **Git tag**: `v` + `appVersion` (currently `v1.10.1`)

`v1.10.0` already exists and ships 1.10.0 / 182. Do **not** move that tag.

### 3. Signing (no prebuild sed)

`app/build.gradle.kts` uses `signingConfigs.maybeCreate("release")` and `signingConfigs.findByName("release")`. F-Droid's scanner may strip the keystore block; configuration must still succeed. **Do not** restore `getByName("release")` — that forced this sed on the fork, which must now be **removed**:

```
prebuild: sed -i -e 's/signingConfigs\.getByName("release")/signingConfigs.maybeCreate("release")/g' build.gradle.kts
```

Local `assembleRelease` still signs when `keystore.properties` or `RELEASE_KEYSTORE_*` env vars point at a real keystore. Never commit a keystore, `keystore.properties`, or passwords.

GitHub Releases for v1.10.0 are signed with a **Dev** certificate (`CN=Edward, OU=Dev`). F-Droid reproducible builds (`Binaries` + `AllowedAPKSigningKeys`) should wait until a **dedicated upload keystore** signs the GitHub APK. After that tagged APK exists:

```yaml
Binaries: https://github.com/edwardlthompson/MultiAppShare-/releases/download/v%v/MultiAppShare-v%v-release.apk
AllowedAPKSigningKeys: <sha256 of the dedicated upload cert>
```

Compute the new cert fingerprint from the signed APK (do not reuse the Dev DN):

```bash
apksigner verify --print-certs MultiAppShare-v1.10.1-release.apk | grep SHA-256
# or
keytool -printcert -jarfile MultiAppShare-v1.10.1-release.apk
```

---

## Pitfalls

### Binary audit

`.gitignore` must ignore `build/` recursively. Never commit `build/` output.

### Image metadata

F-Droid rejects PNGs with extra chunks (EXIF, pHYs, tIME). After adding images:

```bash
python3 app/strip_all_pngs.py
```

### YAML / Fastlane

- No `Summary:` / `Description:` in the YAML (reviewer requirement from MR !34700).
- Phone screenshots live in this repo, not in fdroiddata.
- Changelog files are keyed by **versionCode** (`fastlane/.../changelogs/183.txt`), max 500 characters.

### GitLab `fdroiddata` CI

Copy version/commit/sudo from this repo’s `metadata/` file into the **dedicated fork branch**. Keep the fork’s `AntiFeatures: NonFreeNet` and JDK 21 `sudo` recipe. Drop the `prebuild` sed. Publication still requires an accepted **MR to `fdroid/fdroiddata`**.

---

## Release workflow

1. Bump `val appVersion`, quoted `versionName = "…"`, and `versionCode` together in `app/build.gradle.kts` (F-Droid `checkupdates` will not see `versionName = appVersion`).
2. Add `fastlane/metadata/android/en-US/changelogs/<versionCode>.txt` and a `CHANGELOG.md` section.
3. Strip images: `python3 app/strip_all_pngs.py`.
4. Commit on a feature branch; merge to `main`. **Do not** tag from an agent unless a human asked.
5. Human tags the merge commit (example for this F-Droid polish):

   ```bash
   git fetch origin main
   git log -1 --oneline origin/main
   git tag -a v1.10.1 <sha> -m "Multi App Share 1.10.1"
   git push origin v1.10.1
   ```

6. Update the GitLab fork branch `fdroid/com.edwardlthompson.multiappshare`: set `CurrentVersion` / `CurrentVersionCode` / `Builds.commit` to `v1.10.1`, **delete `prebuild`**. Merge upstream `fdroid/fdroiddata` `master` (merge commit, not rebase). Open the MR from that branch — never from protected `master`.
