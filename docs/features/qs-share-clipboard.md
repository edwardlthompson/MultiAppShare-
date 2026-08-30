# Feature: qs-share-clipboard

> Quick Settings tile for immediate clipboard share. BUILD_PLAN **AE.12**.

## Acceptance criteria

- ✅ `ShareClipboardTileService` provides a Quick Settings tile on supported devices
- ✅ Tapping tile launches `MainActivity` with `ACTION_QS_SHARE_CLIPBOARD`
- ✅ Android 14+ compatible using `PendingIntent` for `startActivityAndCollapse`
- ✅ Manifest declaration with `BIND_QUICK_SETTINGS_TILE` permission

## Tests

- Automated: Tile contract verification

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
