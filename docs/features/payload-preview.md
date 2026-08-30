# Feature: payload-preview

> Show MIME, URI count, and a text snippet before the first sequential handoff. BUILD_PLAN **AE.2**.

## Acceptance criteria

- ✅ User-visible: tapping a group in share mode opens a labelled preview; Continue starts the sequence
- ✅ Cancel / dismiss does not launch a target app
- ✅ Offline: preview is local; no network
- ✅ Accessibility: dialog title and Continue/Cancel are labelled
- ✅ i18n: `preview_*` in `en` / `es` / `fr`

## Smoke scenario

1. _Given_ an inbound `ACTION_SEND` text payload
2. _When_ the user taps a group
3. _Then_ MIME and a text snippet appear before any handoff

## Tests

- Automated: yes — `PayloadPreview` unit tests

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`

## Definition of Done

See `docs/FEATURE_MODULES.md`. BUILD_PLAN **AE.2**.
