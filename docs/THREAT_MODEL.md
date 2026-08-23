# Multi App Share — lightweight threat model

FOSS utility; **no accounts**, **no vendor analytics**. The only in-app network call is an optional once-daily GitHub `releases/latest` GET (User-Agent + 10s timeout) to compare APK filenames. Donate and changelog stay `ACTION_VIEW`.

## Data at rest

| Store | Contents | Risk |
| :--- | :--- | :--- |
| Room DB | Groups, apps (package/activity names), usage counts | Device compromise exposes configuration |
| Internal JSON fallback | Same as above | Same |
| User-initiated **export** | Encrypted JSON envelope (`multiappshare-encrypted-backup`) — passphrase required | Protects cloud/USB copies from casual disclosure |
| `mas_updates` prefs | Last seen version, last check time, dismissed APK version | Device-local; excluded from Auto Backup and encrypted export |
## Data in motion

- **GitHub Releases (optional):** one HTTPS GET to `api.github.com/.../releases/latest` at most every 24 hours. Failures are silent. No analytics payload.
- **User-initiated:** photos/text via **system share intents**, plus `ACTION_VIEW` for Venmo / release APK / Telegram.

## Trust boundaries

- **Passphrase**: chosen by user; not stored by the app; cleared from memory after use (best effort).
- **Imports**: decrypted only in memory for parsing; wrong passphrase fails closed.

## Out of scope

- Protection against rooted-device malware or kernel-level keyloggers (use OS PIN/biometric + device integrity).
