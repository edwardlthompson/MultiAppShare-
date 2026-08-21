# Backup file formats

## Encrypted export (required for menu **Export Groups**)

UTF-8 JSON **envelope** (`application/json`):

- `format`: `"multiappshare-encrypted-backup"`
- `version`: `1`
- `kdf`: `"PBKDF2WithHmacSHA256"`
- `iterations`: `310000`
- `saltB64`, `ivB64`, `ciphertextB64`: standard Base64 strings

Plaintext inside AES-GCM is a **BackupWrapper** JSON (`BackupCodec` / `GroupsRepository`).

Current wrapper **version 2**:

```json
{
  "version": 2,
  "groups": [ { "name": "Social", "apps": [], "isExpanded": false, "usageCount": 0, "id": "uuid" } ],
  "settings": { "darkTheme": true, "appLanguage": "fr", "sharingDelay": 250 },
  "lastPayload": { "uris": [], "text": "hello", "mimeType": "text/plain" }
}
```

`settings` and `lastPayload` are optional. Envelope `format` / AES-GCM fields are unchanged.

## Legacy import (still supported)

Plain UTF-8 JSON:

- `{ "version": 1, "groups": [ … ] }` (no settings / last payload), or
- Legacy raw `[ … ]` array of groups

v1 / raw-array import **generates** missing group ids and **does not** overwrite current settings or last-share payload.

Used for older backups before encryption was required.
