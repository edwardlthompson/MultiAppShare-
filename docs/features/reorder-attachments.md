# Feature: reorder-attachments

> Reorder `ACTION_SEND_MULTIPLE` URIs before the first handoff. BUILD_PLAN **AE.3**.

## Acceptance criteria

- ✅ User-visible: two or more attachments offer an order dialog after preview
- ✅ Cancel leaves the session unstarted
- ✅ Offline: local only
- ✅ Accessibility: move up/down labelled
- ✅ i18n: `preview_reorder_*` / `preview_move_*`

## Tests

- Automated: `PayloadReorderTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
