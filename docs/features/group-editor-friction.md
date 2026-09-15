# Feature: group-editor-friction

> Modify-apps uses full labels, Select all, and an empty-search sentence. BUILD_PLAN **UX-057**.

## Acceptance criteria

- ✅ Dialog is full-size (`fillMaxSize`; no 300.dp list height)
- ✅ Chips use `GroupEditorSearch.chipLabel` (no `split(" - ")`)
- ✅ Select all adds currently visible rows
- ✅ Empty search shows `no_apps_match_search`

## Tests

- Automated: `GroupEditorSearchTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
