# Feature: package-change-listener

> Refresh icons and app groups when an application is installed, updated, or removed. BUILD_PLAN **AE.14**.

## Acceptance criteria

- ✅ `PackageChangeFilter.isRelevantAction` identifies package lifecycle broadcast actions
- ✅ `PackageChangeReceiver` listens for package additions, removals, changes, and replacements
- ✅ Receiver notifies listener to refresh app lists and group compatibility
- ✅ Local/offline broadcast handling

## Tests

- Automated: `PackageChangeFilterTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
