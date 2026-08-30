# Feature: pause-resume-share

> Pause mid-sequence without Skip remaining. BUILD_PLAN **AE.4**.

## Acceptance criteria

- ✅ Pause disables Next / Skip (and notification Next)
- ✅ Resume restores those actions; remaining apps stay queued
- ✅ Pause persists on the in-flight snapshot
- ✅ i18n: `sharing_pause` / `sharing_resume`

## Tests

- Automated: `SharePauseTest` + snapshot round-trip

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
