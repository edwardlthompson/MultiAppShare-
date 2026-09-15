# Feature: share-return-copy

> Overlay return instructions without URI schemes. BUILD_PLAN **UX-011**.

## Acceptance criteria

- ✅ `sharing_return_instruction` is at most two sentences
- ✅ No `://` scheme in en / es / fr overlay copy
- ✅ Deeplink stays out of the overlay (About / debug only)

## Tests

- Automated: `ShareReturnCopyTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
