# Android Auto Backup vs manual export

This app stores **groups**, **share history**, and **settings** on device. Understand what Google/cloud backup restores versus what you control via **Settings → export/import**.

## What lives where

| Data | Primary store | Legacy JSON file (files dir) |
| :--- | :--- | :--- |
| Groups | Room (`multiappshare_db`) + mirror `groups.json` | `groups.json` (auto-updated when groups change) |
| History | Room + mirror `history.json` | `history.json` |
| Preferences | DataStore | — |

Room database files live under the app’s **internal storage** (same filesystem domain as `files/`).

## Cloud backup & device transfer (`data_extraction_rules.xml`)

The manifest references `android:dataExtractionRules="@xml/data_extraction_rules"`. The current rules **include** `domain="file"` with `path="."`, which covers **files under the app’s device-protected / credential-encrypted storage that participate in backup**, including internal database and JSON mirrors—subject to OEM timing and user backup settings.

**Implication:** After a **cloud restore** or **device-to-device transfer**, users may get **Room data and/or JSON mirrors** together. Your repositories already prefer **Room when non-empty**, then fall back to legacy JSON migration paths.

## Manual encrypted export/import

User-driven **export** produces an encrypted payload (see `BackupCipher` / settings UI). Inner plaintext is **BackupWrapper v2** (groups with stable `id`, optional settings, optional last-share payload). Import of v1 / raw arrays still works and does not overwrite settings. Envelope format is unchanged. That path is **independent** of Android Auto Backup: it is explicit, passphrase-protected, and intended for **user-controlled** backup and cross-device moves without trusting cloud plaintext.

## Avoiding surprises (checklist)

1. When bumping **Room schema version**, ship **migrations** for release builds (see `DatabaseModule` and `BUILD_PLAN` **F.2**). Debug builds may use destructive migration for speed.
2. After changing backup rules, smoke-test: install → add groups → **adb backup** / Pixel restore simulation if you rely on cloud restore for your own devices.
3. Document for end users: **cloud restore** can lag or be disabled; **manual export** is the guaranteed portability path.
