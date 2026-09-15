# Feature: auto-group-icon

> Auto-group uses the same AutoAwesome icon as onboarding, not a wrench. BUILD_PLAN **UX-045**.

## Acceptance criteria

- ✅ Overflow auto-group leading icon is `Icons.Default.AutoAwesome`
- ✅ TalkBack uses `fab_auto_group` as the icon content description
- ✅ `OverflowMenuPolicy` icon key for AUTO_GROUP is `auto_awesome`

## Tests

- Automated: `OverflowMenuPolicyTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
