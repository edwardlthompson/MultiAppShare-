# Feature: sharing-delay-labels

> Share delay presets use camera-timer labels, never milliseconds. BUILD_PLAN **UX-044**.

## Acceptance criteria

- ✅ Presets map to None / Short (0.5s) / 1 second / N seconds via `SharingDelay.presetCopy`
- ✅ No `delay_ms` or “ms” in user-facing delay copy
- ✅ Storage remains milliseconds internally (`SharingDelay.PRESETS_MS`)

## Tests

- Automated: `SharingDelayTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
