# Feature: backup-payload-opt-in

> Toggle including the last session payload in encrypted and plaintext backups. BUILD_PLAN **AE.31**.

## Acceptance criteria

- ✅ `BackupPayloadOptIn.resolvePayloadForBackup` conditionally strips payloads from backups when opt-in is disabled
- ✅ Preserves valid non-empty payloads when opt-in is explicitly enabled
- ✅ Zero leakage of cached share payloads by default

## Tests

- Automated: `BackupPayloadOptInTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
