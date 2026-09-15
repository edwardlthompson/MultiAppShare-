# Feature: overlay-session-copy

> Share overlay subtitle is “Tap a group to start.” Order help stays on home, not in the incoming-share chrome. BUILD_PLAN **UX-080**.

## Acceptance criteria

- ✅ `share_overlay_subtitle` has no overflow glyph and does not mention the home screen
- ✅ Group order remains available on home (top-bar reorder / overflow Sort Groups)
- ✅ All shipped locales match `ShareOverlayCopy.isSessionScoped`

## Tests

- Automated: `ShareOverlayCopyTest`, `ShareOverlayCopyFilesTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
