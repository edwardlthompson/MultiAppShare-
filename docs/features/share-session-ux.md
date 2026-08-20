# Feature: share-session-ux

## Acceptance criteria

- ✅ User-visible: resume after process death; History re-share last; skip remaining; duplicate group; tablet two-pane; Back leaves overlay; language picker
- ✅ Offline/error: stale snapshot ignored; dead last-payload toasts
- ✅ Accessibility: finish-early and language radios are labeled buttons
- ✅ i18n: keys in `values`, `values-fr`, `values-es`

## Smoke scenario

1. Share a photo into Multi App Share, start a group, force-stop the app, reopen — step overlay returns.
2. History → Re-share last → pick a group.
3. Mid-sequence tap Skip remaining.
4. Duplicate a group; on a wide window confirm list + detail panes.
5. Menu → Language → Français and confirm strings update.

## Definition of Done

Unit tests for snapshot nonce/freshness, copy names, and duplicate mutation. `feature-gate.sh --stack android` after AGENT work.
