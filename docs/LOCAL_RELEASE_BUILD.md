# Local signed release builds (APK / App Bundle)

**New to signing keys?** Start with **[SIGNING_FOR_BEGINNERS.md](SIGNING_FOR_BEGINNERS.md)** (simple steps and **`scripts/setup-release-keystore.ps1`**). This page is the slightly more detailed reference.

Use this when you want a **properly signed** `release` APK or AAB on your machine—for **adb sideload testing**, **GitHub Releases**, **F-Droid reproducible builds**, or Play uploads (**AAB**).

## 1. One-time keystore

From the repo root:

```bash
keytool -genkeypair -v -keystore release.keystore -alias multiappshare \
  -keyalg RSA -keysize 2048 -validity 10000
```

Keep **`release.keystore`** secret and **back it up** (loss = you cannot ship updates under the same signing key).

## 2. Signing configuration (do not commit secrets)

1. Copy **`keystore.properties.example`** → **`keystore.properties`** at the repo root (already **gitignored**).
2. Fill in **`storePassword`**, **`keyAlias`**, **`keyPassword`**, and **`storeFile`** if your keystore path differs.

Environment variables override the file (useful for CI or one-off shells):

| Variable | Meaning |
| :--- | :--- |
| `RELEASE_KEYSTORE_PATH` | Path to `.keystore` (relative to repo root or absolute) |
| `RELEASE_KEYSTORE_PASSWORD` | Keystore password |
| `RELEASE_KEY_ALIAS` | Key alias |
| `RELEASE_KEY_PASSWORD` | Key password |

## 3. Build artifacts

**Signed APK** (matches **`base.archivesName`** in `app/build.gradle.kts`, typically under `app/build/outputs/apk/release/`):

```bash
./gradlew :app:assembleRelease
```

**Signed Android App Bundle** (Play Console):

```bash
./gradlew :app:bundleRelease
```

If **`storeFile`**, passwords, or alias are missing or wrong, the **`release`** build type falls back to **no release signing config**; you still get a release binary from Gradle, but it will **not** match your release key—use **`keystore.properties`** before attaching artifacts to GitHub Releases.

## 4. Install over adb

Example after **`assembleRelease`** (adjust version in the filename if needed):

```bash
adb install -r app/build/outputs/apk/release/MultiAppShare-v1.8.0-release.apk
```

If the device has another signing key installed for the same **`applicationId`**, uninstall first or fix **`INSTALL_FAILED_UPDATE_INCOMPATIBLE`** as in **`docs/BASELINE_PROFILE.md`**.

## 5. Convenience script (Windows)

From the repo root:

```powershell
.\scripts\build-signed-release-apk.ps1
```

The script runs **`assembleRelease`** and prints the path to the release APK. It does not create a keystore for you.
