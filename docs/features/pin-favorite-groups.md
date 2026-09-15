# Feature: pin-favorite-groups

> Pin favorite groups to stay at the top of the group list above standard usage sort. BUILD_PLAN **AE.19**.
>
> Layer: user-visible (`GroupItemOverflowMenu`). Domain: `GroupPinOrder`.

## Acceptance criteria

- ✅ User-visible: pin/unpin on the group overflow menu (`GroupItemOverflowMenu`)
- ✅ Domain: `GroupPinOrder.sortGroupsWithPinned` partitions pinned groups to the top, preserving usage sort within both partitions
- ✅ Domain: `GroupPinOrder.togglePin` adds and removes group names from the pinned set
- ✅ Fully reactive and offline

## Tests

- Automated: `GroupPinOrderTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
