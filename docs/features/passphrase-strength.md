# Feature: passphrase-strength

> Strength meter and validation levels on backup passphrase. BUILD_PLAN **AE.32**.

## Acceptance criteria

- ✅ `PassphraseStrengthMeter.evaluate` grades passphrases into EMPTY, WEAK, FAIR, and STRONG tiers based on length and character entropy
- ✅ Avoids converting `CharArray` to immutable `String` where possible to minimize memory retention
- ✅ Real-time feedback support for backup encryption dialogs

## Tests

- Automated: `PassphraseStrengthMeterTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
