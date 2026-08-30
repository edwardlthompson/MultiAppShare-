# Feature: theme-toggle-labels

> Full-row selectable TalkBack names and merged checkbox semantics on crash/refresh/haptics toggles in ThemeDialog. BUILD_PLAN **AE.52**.

## Acceptance criteria

- ✅ Checkbox in `ThemeDialog` delegates click handling to row-level `Modifier.selectable` with `onCheckedChange = null`
- ✅ TalkBack announces the entire label together with the checkbox state in a single accessibility node
- ✅ Eliminates dual-focus accessibility traps

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
