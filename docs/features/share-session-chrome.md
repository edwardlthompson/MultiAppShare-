# Feature: share-session-chrome

> In-app share bar matches the FGS notification step identity: Sharing · 2 of 5 · {app}, plus Close. BUILD_PLAN **UX-075**.

## Acceptance criteria

- ✅ Choose-group mode keeps `share_overlay_title` and Close
- ✅ After start, bar title uses `share_session_chrome_title` from `ShareSessionChrome.identity`
- ✅ Notification content title uses the same identity

## Tests

- Automated: `ShareSessionChromeTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
