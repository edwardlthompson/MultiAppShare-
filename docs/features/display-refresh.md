# Feature: display-refresh

> High-refresh / display mode selector (phones and tablets only). **Do not** copy `examples/android/` over `app/`. Stub: template `docs/features/display-refresh.md`.

## Acceptance criteria

- ✅ User-visible: optional display-refresh preference (system default vs high) if the device supports it
- ✅ Offline: preference is local; no network
- ✅ Safe fallback: unsupported devices keep default refresh; no crash
- ✅ Accessibility: setting control is labelled
- ✅ i18n: `display_*` in `en` / `es` / `fr`

## Smoke scenario

1. _Given_ a device/emulator with standard refresh
2. _When_ the user leaves the default
3. _Then_ the app still renders; no exception in logcat

## Container map

| Layer | Path |
|-------|------|
| Logic | `app/.../display/DisplayModeSelector.kt` (unit-testable selection) |
| View | Settings (AD.2) row or existing theme host |
| Tests | `DisplayModeSelector` unit tests |
| Wiring | `MainActivity` / window ≤10 lines |

## Tests

- Automated: yes — mode selector + fallback

## Fallback validation

- Why tests are not feasible: N/A (automated tests exist)
- Command: `bash scripts/feature-gate.sh --stack android`

## Definition of Done

See `docs/FEATURE_MODULES.md`. One `/feature` task. BUILD_PLAN **AD.7**.
