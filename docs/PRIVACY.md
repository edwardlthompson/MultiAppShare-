# Privacy — Multi App Share

**Privacy-by-design:** no accounts, no backend, no analytics, no proprietary tracking SDKs.

## Data collected

- **On device only:** app groups, share history, preferences (Room + DataStore)
- **Optional export:** user-initiated encrypted JSON backup (AES-256-GCM) — see [`BACKUP_FORMAT.md`](BACKUP_FORMAT.md)
- **No network transmission** of user content by the app

## Permissions

Used only for core functionality (share intents, foreground service during sequential share, optional storage for backup import/export). See `AndroidManifest.xml` and [`THREAT_MODEL.md`](THREAT_MODEL.md).

## Third parties

No Firebase, GMS analytics, or closed telemetry. OSS libraries listed in [`THIRD_PARTY_LICENSES.md`](../THIRD_PARTY_LICENSES.md).

## Contact

Security issues: [`SECURITY.md`](../SECURITY.md).
