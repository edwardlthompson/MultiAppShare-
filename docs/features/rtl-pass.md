# Feature: rtl-pass

> RTL layout audit and direction awareness for overlay dialogs, steps, and directional iconography. BUILD_PLAN **AE.51**.

## Acceptance criteria

- ✅ `RtlLayoutAudit.isRtlLocale` identifies standard right-to-left language locales (Arabic, Hebrew, Persian, Urdu, etc.)
- ✅ UI layout constructs use start/end alignment rather than rigid left/right constraints
- ✅ AutoMirrored directional vector icons mirror properly under RTL

## Tests

- Automated: `RtlLayoutAuditTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
