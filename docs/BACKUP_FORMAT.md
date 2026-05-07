# Backup file formats

## Encrypted export (required for menu **Export Groups**)

UTF-8 JSON **envelope** (`application/json`):

- `format`: `"multiappshare-encrypted-backup"`
- `version`: `1`
- `kdf`: `"PBKDF2WithHmacSHA256"`
- `iterations`: `310000`
- `saltB64`, `ivB64`, `ciphertextB64`: standard Base64 strings

Plaintext inside AES-GCM is a **BackupWrapper** JSON (see `GroupsRepository`): `{ "version": 1, "groups": [ … ] }`.

## Legacy import (still supported)

Plain UTF-8 JSON:

- `{ "version": 1, "groups": [ … ] }`, or  
- Legacy raw `[ … ]` array of groups  

Used for older backups before encryption was required.
