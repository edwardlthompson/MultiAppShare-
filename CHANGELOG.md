# Changelog

This project uses Android **`versionCode`**-keyed changelogs for F-Droid/Store metadata under
`fastlane/metadata/android/en-US/changelogs/`.

## [Unreleased]

### Added

- About changelog link and in-app Feedback that opens a sanitized GitHub issue form (`ACTION_VIEW` only).
- Opt-in “save crash details for review” with a local review dialog; nothing is uploaded automatically.
- Theme/settings toggles for crash-save (default off) and high refresh rate (safe fallback).
- F-Droid installs get an F-Droid listing prompt, never a GitHub APK Install button.
- Share payload preview (MIME, attachment count, text snippet) before the first handoff.
- Reorder multiple share attachments before the first handoff.

### Changed

### Fixed

## 1.9.5 (versionCode 181)

### Added

- Quiet **Donate via Venmo** in the app menu and About (never on the update dialog).
- One optional post-update donate note per installed version.
- Once-daily GitHub Releases check that compares product APK filenames and offers **Install** | **Later**.

## 1.9.4 (versionCode 180)

### Added

- Stable group `id` (Room v2) so shortcuts and deeplinks survive rename; history rows can store a re-share payload.
- Per-row History re-share; overflow share-from-clipboard (text or content URI).
- Encrypted backup wrapper v2: settings (theme, language, delay) and last payload; v1 import still works.

### Changed

- Group filter shows whenever any groups exist (not only when there are more than eight).
- Failed share handoff no longer auto-advances; Retry stays on the same app.

## 1.9.3 (versionCode 179)

### Added

- Resume an in-flight share after process death (DataStore snapshot, 2-hour expiry, payload nonce).
- Re-share last payload from History.
- Skip remaining apps mid-sequence; system Back exits overlay or finishes early.
- Duplicate group; tablet two-pane group editor; in-app language (en/fr/es/system).
- Persist persistable inbound content URI grants; rename and merge groups; undo delete.
- Theme (system/light/dark); delay between share steps; skip this app; notification Next/Skip/Cancel.

## 1.9.2 (versionCode 178)

### Added

- Prefer the display's fastest refresh rate at the current resolution; mark list/dialog scroll surfaces for high-refresh flings.
- Agent-project-bootstrap **v0.21.0** sync: `/coach` `/tour` `/ideas` `/codex-review`, multi-agent adapters, `verify.sh`, `bootstrap.config.json`.

### Changed

- Batch commands: 30 slash commands (25 atomic + 5 super).

## 1.9.1 (versionCode 177)

### Added

- Agent-project-bootstrap **v0.15.0** alignment (Milestone Y): Cursor skills/hooks/agents, `/cleanup`, `TEMPLATE_INDEX.json`, template update checker, `HUMAN_BACKLOG.md`.
- GitHub workflows: `dependency-review.yml`, `stale.yml`, android-adapted `weekly-health-check.yml`.

### Changed

- Batch commands: 26 slash commands (21 atomic + 5 super); validate-bootstrap uses parallel local checks.
- Docs: `BOOTSTRAP_TEMPLATE_MAP` / `BOOTSTRAP_ALIGNMENT` for android-child divergences (`docs/` board paths, `android.yml`).

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
- Local signing docs + helper scripts for building signed release APKs.
