# Feature: touch-targets

> 48dp minimum touch targets for reorder, chip remove, and settings radios. BUILD_PLAN **UX-027**.

## Acceptance criteria

- ✅ Reorder up/down `IconButton`s use the default 48dp target (no `size(36.dp)`)
- ✅ Selected-app remove is an `IconButton` with TalkBack name, not a 16dp chip icon
- ✅ Theme, Language, and Delay option rows use `defaultMinSize(minHeight = Dimens.TouchTarget)`
- ✅ Drag-handle 22dp is gone (replaced by up/down in UX-006)

## Tests

- Automated: `ThemeTokensTest.touchTargetIsAtLeast48dp`

## Fallback validation

- Command: `./gradlew :app:testDebugUnitTest --tests com.multiappshare.ThemeTokensTest`
