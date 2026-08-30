# Feature: feedback

> In-app feedback dialogs. **Do not** copy `examples/android/` over `app/`. Stub: template `docs/features/feedback.md`.

## Acceptance criteria

- 🔲 User-visible: Feedback entry from About or overflow; preview before send
- 🔲 Offline/error: preview works offline; send failure does not crash the app
- 🔲 No PII in the default body; user must opt in to attach crash text (AD.3)
- 🔲 Accessibility: dialog role + labelled buttons
- 🔲 i18n: `feedback_*` in `en` / `es` / `fr`

## Smoke scenario

1. _Given_ the dashboard
2. _When_ the user opens Feedback
3. _Then_ a labelled dialog appears and Cancel/dismiss does not navigate away unexpectedly

## Container map

| Layer | Path |
|-------|------|
| Logic | new `core-domain/.../feedback/` or `app/.../feedback/` preview helpers |
| View | `app/.../ui/feedback/` or `feature-dashboard/` dialog |
| Tests | unit tests for preview/prefs |
| Wiring | overflow/About ≤10 lines |

## Tests

- Automated: yes — preview + prefs

## Fallback validation

- Why tests are not feasible: N/A (automated tests exist)
- Command: `bash scripts/feature-gate.sh --stack android`

## Definition of Done

See `docs/FEATURE_MODULES.md`. One `/feature` task. BUILD_PLAN **AD.4**.
