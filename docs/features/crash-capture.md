# Feature: crash-capture

> Opt-in local crash queue. Never auto-sends. Sanitize before persist. **Do not** copy `examples/android/` over `app/`. Stub: template `docs/features/crash-capture.md`. Depends on AD.2 toggle and AD.4 dialog contract.

## Acceptance criteria

- 🔲 User-visible: when save-crashes is on, one review dialog after a captured crash; never auto-open GitHub
- 🔲 Setting off: nothing persisted; turning off deletes any stored record
- 🔲 Offline/error: write failure drops the record; handler errors do not re-enter
- 🔲 Accessibility: same dialog contract as Feedback
- 🔲 i18n: `feedback_*` / `crash_*` in `en` / `es` / `fr`

## Smoke scenario

1. _Given_ save-crashes is off
2. _When_ an unhandled error occurs
3. _Then_ nothing is persisted
4. _When_ the setting is on, at most one sanitized record is stored

## Container map

| Layer | Path |
|-------|------|
| Logic | new `core-domain/.../crashcapture/` (pure Kotlin) |
| Prefs | `app/` or `:core-database` DataStore — not encrypted backup export |
| View | `feature-dashboard/` or `app/.../ui/` review dialog |
| Tests | `core-domain/src/test/.../crashcapture/` |
| Wiring | composition root ≤10 lines |

## Tests

- Automated: yes — pending-crash allowlist, sanitize, toggle-off deletes

## Fallback validation

- Why tests are not feasible: N/A (automated tests exist)
- Command: `bash scripts/feature-gate.sh --stack android`

## Definition of Done

See `docs/FEATURE_MODULES.md`. One `/feature` task. BUILD_PLAN **AD.3**.
