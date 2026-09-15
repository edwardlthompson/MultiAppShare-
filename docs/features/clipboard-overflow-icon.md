# Feature: clipboard-overflow-icon

> Share clipboard uses ContentPaste; History uses History. BUILD_PLAN **UX-068**.

## Acceptance criteria

- ✅ Overflow iconKey for SHARE_CLIPBOARD is `content_paste`
- ✅ Overflow iconKey for HISTORY is `history`
- ✅ Icon keys remain unique

## Tests

- Automated: `OverflowMenuPolicyTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
