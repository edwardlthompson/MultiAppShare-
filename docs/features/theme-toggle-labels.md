# Feature: theme-toggle-labels

> Full-row selectable TalkBack names on crash/refresh/haptics toggles. BUILD_PLAN **AE.52** / **UX-009**.

## Acceptance criteria

- ✅ Switch rows in Settings detail use `Modifier.selectable` with `onCheckedChange = null`
- ✅ Theme dialog is appearance radios only (UX-009)
- ✅ TalkBack announces the entire label together with the checkbox state in a single accessibility node
- ✅ Eliminates dual-focus accessibility traps

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
