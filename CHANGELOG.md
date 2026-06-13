# Changelog

This project uses Android **`versionCode`**-keyed changelogs for F-Droid/Store metadata under
`fastlane/metadata/android/en-US/changelogs/`.

## 1.9.0 (versionCode 176)

- **Data integrity:** Room replace-all sync for groups and history; duplicate group names rejected; corrupt app-list JSON logged.
- **Backup hardening:** Export passphrase race fix, null-stream handling, 4 MB import size cap.
- **Sharing reliability:** Share session survives rotation; SEND intent resets prior session; failed targets auto-skip; improved activity resolution.
- **Onboarding & deeplinks:** Deferred auto-group until user consent; deeplink group expand queued until data loads.
- **Dashboard:** Completed `DashboardViewModel.loadData()`; history/about dialogs migrated to `:feature-dashboard`.
- **Security:** Plaintext JSON shadow backups excluded from Android Auto Backup; JSON backup failures logged.
- **CI & quality:** CodeQL, detekt, Kover coverage, file-size limits, Paparazzi accessibility snapshots; agent workspace docs (Milestones R + S).

## 1.8.0 (versionCode 175)

- Baseline profile generation + merged artifacts.
- Encrypted JSON backups (passphrase-based AES-GCM) + tests.
- Hilt + Room migrated to KSP; toolchain and CI hygiene improvements.
- French + Spanish localizations; locale config.
- Local signing docs + helper scripts for building signed APKs.

