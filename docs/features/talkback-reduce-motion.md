# Feature: talkback-reduce-motion

> Labeled onboarding dots, state-aware expand/collapse, reduce-motion pager. BUILD_PLAN **UX-029**.

## Acceptance criteria

- ✅ Onboarding pager dots are 48dp `IconButton`s announcing “Page N of 2”
- ✅ Group expand/collapse TalkBack names are `cd_expand_group` / `cd_collapse_group` with the group name
- ✅ FAB already uses `fab_add_group`; overflow leading icons use the row label
- ✅ Onboarding pager uses `scrollToPage` when `ReduceMotion.skipBurst` is true
- ✅ `ACCESSIBILITY_CHECKLIST.md` device rows stay `[USER-SKIP]` / `[HUMAN]`

## Tests

- Automated: existing `ReduceMotionTest`; Paparazzi `OnboardingDialogPaparazziTest`

## Fallback validation

- Command: `./gradlew :core-domain:test --tests com.multiappshare.reducemotion.ReduceMotionTest`
