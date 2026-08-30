# Feature: github-feedback

> GitHub issue composer (form URL). **Do not** copy `examples/android/` over `app/`. Stub: template `docs/features/github-feedback.md`. Depends on AD.4.

## Acceptance criteria

- ✅ User-visible: Feedback can open a GitHub issue form via `ACTION_VIEW` (no raw token, no phone-home API)
- ✅ Offline/error: if URI build fails, stay on the preview; never crash
- ✅ Title/body come from the preview; crash payload only if user opted in (AD.3 / AD.6)
- ✅ Accessibility: the GitHub action is a labelled button
- ✅ i18n: reuse `feedback_*`

## Smoke scenario

1. _Given_ Feedback preview is open
2. _When_ the user chooses the GitHub action
3. _Then_ an `ACTION_VIEW` intent targets the public issue form (or a clear in-app failure)

## Container map

| Layer | Path |
|-------|------|
| Logic | `app/.../githubfeedback/IssueFormUrl.kt` (pure URL builder, testable) |
| View | Feedback dialog action |
| Tests | `IssueFormUrl` unit tests |
| Wiring | ≤10 lines on the Feedback dialog |

## Tests

- Automated: yes — URL builder (repo, labels, length limits)

## Fallback validation

- Why tests are not feasible: N/A (automated tests exist)
- Command: `bash scripts/feature-gate.sh --stack android`

## Definition of Done

See `docs/FEATURE_MODULES.md`. One `/feature` task. BUILD_PLAN **AD.5**.
