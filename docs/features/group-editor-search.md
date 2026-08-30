# Feature: group-editor-search

> Filter installed apps in the group editor by app name or package name. BUILD_PLAN **AE.15**.

## Acceptance criteria

- ✅ `GroupEditorSearch.filter` filters installed apps case-insensitively by app name or package name
- ✅ Empty/blank query returns unfiltered app list
- ✅ `ModifyGroupAppsDialog` uses `GroupEditorSearch.filter` for immediate search response

## Tests

- Automated: `GroupEditorSearchTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
