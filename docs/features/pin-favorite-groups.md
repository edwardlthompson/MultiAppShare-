# Feature: pin-favorite-groups

> Pin favorite groups to stay at the top of the group list above standard usage sort. BUILD_PLAN **AE.19**.

## Acceptance criteria

- ✅ `GroupPinOrder.sortGroupsWithPinned` partitions pinned groups to the top, preserving usage sort within both partitions
- ✅ `GroupPinOrder.togglePin` adds and removes group names from the pinned set
- ✅ Fully reactive and offline

## Tests

- Automated: `GroupPinOrderTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
