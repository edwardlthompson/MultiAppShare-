# Feature: donate-on-about-only

> Fundraising is not an overflow peer. Donate lives on About. BUILD_PLAN **UX-061**.

## Acceptance criteria

- ✅ Overflow destinations do not include Donate (`OverflowMenuPolicy.includesDonate` is false)
- ✅ Settings hub list has no donate row
- ✅ About still exposes Venmo (`about_donate`)

## Tests

- Automated: `OverflowMenuPolicyTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
