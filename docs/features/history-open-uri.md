# Feature: history-open-uri

> Open original shared URI from history records when persistable permission grants remain valid. BUILD_PLAN **AE.30**.

## Acceptance criteria

- ✅ `HistoryUriPermission.canOpenUri` verifies content URI access against persisted grant sets
- ✅ Handles web HTTP/HTTPS links automatically
- ✅ Rejects stale or revokable content URIs before launching intents

## Tests

- Automated: `HistoryUriPermissionTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
