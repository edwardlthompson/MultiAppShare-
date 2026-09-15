# Feature: deeplink-missing-group

> Missing `multiappshare://group` targets explain themselves. QS tile is inactive with empty clipboard. Home widget already ships with i18n. BUILD_PLAN **UX-035**.

## Acceptance criteria

- ✅ Unknown group id/name sets `missingDeepLinkLabel` and shows a snackbar with Create
- ✅ Already-expanded groups are treated as found (no false missing snackbar)
- ✅ QS tile `STATE_INACTIVE` + `qs_clipboard_empty` when clipboard has no clip
- ✅ Home widget uses `widget_recent_group` / `widget_clipboard` in en/de/es/fr/it

## Tests

- Automated: `DeepLinkGroupLookupTest`, `QsClipboardTileModelTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
