# Feature: group-overflow-sections

> Per-group ⋮ is grouped Edit · Organize · Delete. BUILD_PLAN **UX-066**.

## Acceptance criteria

- ✅ `GroupOverflowPolicy.sections` is Edit, Organize, Danger
- ✅ Delete is the only danger item and stays error-colored
- ✅ `HorizontalDivider` between sections in `GroupItemOverflowMenu`

## Tests

- Automated: `GroupOverflowPolicyTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
