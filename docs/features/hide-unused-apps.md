# Feature: hide-unused-apps

> Global hide list for unwanted or system apps in the app picker. BUILD_PLAN **AE.23**.

## Acceptance criteria

- ✅ `HiddenAppsFilter.filterVisibleApps` removes packages in the user's hidden set from the selection pool
- ✅ `HiddenAppsFilter.toggleHidden` adds or removes a package from the hidden set
- ✅ Fully local and private state management

## Tests

- Automated: `HiddenAppsFilterTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
