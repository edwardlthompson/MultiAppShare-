# Multi App Share — lightweight threat model

FOSS utility; **no accounts**, **no vendor analytics**, **no network API** in-app.

## Data at rest

| Store | Contents | Risk |
| :--- | :--- | :--- |
| Room DB | Groups, apps (package/activity names), usage counts | Device compromise exposes configuration |
| Internal JSON fallback | Same as above | Same |
| User-initiated **export** | Encrypted JSON envelope (`multiappshare-encrypted-backup`) — passphrase required | Protects cloud/USB copies from casual disclosure |

## Data in motion

- **None** by the app itself (no telemetry SDK). User shares photos/text via **system share intents** — standard Android behavior.

## Trust boundaries

- **Passphrase**: chosen by user; not stored by the app; cleared from memory after use (best effort).
- **Imports**: decrypted only in memory for parsing; wrong passphrase fails closed.

## Out of scope

- Protection against rooted-device malware or kernel-level keyloggers (use OS PIN/biometric + device integrity).
