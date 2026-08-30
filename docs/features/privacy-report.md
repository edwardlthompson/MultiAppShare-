# Feature: privacy-report

> On-device privacy sanitizer for crash/feedback text. **Do not** copy `examples/android/` over `app/`. Stub: template `docs/features/privacy-report.md`. Shared by AD.3 and AD.5.

## Acceptance criteria

- ✅ User-visible: any persisted or GitHub-bound crash/feedback text is sanitized (emails, tokens, prompt-injection phrases redacted)
- ✅ Offline: sanitizer is local; no network
- ✅ Fail-safe: sanitizer errors drop the payload rather than write raw text
- ✅ Accessibility: N/A for logic; any export UI stays labelled
- ✅ i18n: user-facing copy under `privacy_*` if a report screen is added

## Smoke scenario

1. _Given_ a fixture string with an email and a `ghp_` token
2. _When_ sanitize runs
3. _Then_ those spans are redacted in the output

## Container map

| Layer | Path |
|-------|------|
| Logic | `core-domain/.../privacyreport/` (pure Kotlin) |
| Tests | `core-domain/src/test/.../privacyreport/` + `schemas/golden-path/sanitize-fixtures.json` |
| View | optional; prefer logic-only unless a report screen is needed |
| Wiring | called from crash-capture / github-feedback only |

## Tests

- Automated: yes — fixture-driven sanitize tests

## Fallback validation

- Why tests are not feasible: N/A (automated tests exist)
- Command: `bash scripts/feature-gate.sh --stack android`

## Definition of Done

See `docs/FEATURE_MODULES.md`. One `/feature` task. BUILD_PLAN **AD.6**.
