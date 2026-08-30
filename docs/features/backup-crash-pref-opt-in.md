# Feature: backup-crash-pref-opt-in

> Optional inclusion of crash-save preference in backup payloads. BUILD_PLAN **AE.35**.

## Acceptance criteria

- ✅ `BackupSettings.crashCaptureEnabled` serializes and deserializes cleanly without breaking backward compatibility
- ✅ Retains nullability when unset in legacy backups
- ✅ Pure data serialization

## Tests

- Automated: `BackupCodecTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
