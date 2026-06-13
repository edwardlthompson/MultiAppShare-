# Knowledge base — Multi App Share

Project-specific edge cases and resolved patterns. Not generic framework docs.

## Sequential sharing and OEM variance

Share targets are **opaque** — the app only relaunches `ACTION_SEND` for the current orchestration index; it cannot undo inside another app's composer. See [`docs/MANUAL_SHARE_CHECKLIST.md`](docs/MANUAL_SHARE_CHECKLIST.md).

Aggressive battery savers may kill the foreground service. Document device matrix in [`docs/OEM_BATTERY.md`](docs/OEM_BATTERY.md). Optional user-initiated "ignore battery optimizations" copy only — never auto-request.

## Foreground service notification policy

`SharingService` uses **low importance** channel (`sharing_service_channel_v2`). Do **not** raise importance to peek over target apps' compose fields. Notification body includes "Open Multi App Share to continue" — see [`docs/RETURN_PATH.md`](docs/RETURN_PATH.md).

## Process death mid-sequence

Session is **ephemeral** — if the system kills the app mid-share, user re-shares after cold start. See [`docs/PROCESS_DEATH.md`](docs/PROCESS_DEATH.md).

## Encrypted backup envelope

Format: UTF-8 JSON wrapper `multiappshare-encrypted-backup` with AES-256-GCM + PBKDF2-HMAC-SHA256 (310k iterations). Wrong passphrase fails closed. Legacy plaintext JSON imports still supported. See [`docs/BACKUP_FORMAT.md`](docs/BACKUP_FORMAT.md) and `BackupCipher.kt`.

## Deeplinks (no Play Services)

Custom scheme only: `multiappshare://open`, `multiappshare://group?name=…`. No verified App Links unless a real domain is added later.

## No overlay on other apps

True floating bubble over Instagram/Twitter requires `SYSTEM_ALERT_WINDOW` — **out of scope**. Return paths: notification tap, Recents, deeplink, home shortcut.

## Test doubles (deterministic mocking)

Use Robolectric + MockK for ViewModels — see [`MainViewModelTest.kt`](app/src/test/java/com/multiappshare/MainViewModelTest.kt). In-memory Room for repository tests. Paparazzi for stable Composables — [`docs/PAPARAZZI.md`](docs/PAPARAZZI.md).

## Room migrations

Never use `fallbackToDestructiveMigration()` in release. Export schemas to `core-database/schemas/`. Checklist: [`docs/ROOM_MIGRATION_CHECKLIST.md`](docs/ROOM_MIGRATION_CHECKLIST.md).

## F-Droid PNG metadata

Run [`app/strip_all_pngs.py`](app/strip_all_pngs.py) before release builds when PNGs change — F-Droid rejects EXIF/timestamp metadata in shipped assets.
