# Feature: loading-success-motion

> Group-row skeletons and a 300ms theme-token success check. BUILD_PLAN **UX-030**.

## Acceptance criteria

- ✅ Loading uses `GroupListSkeleton` cards, not a centered spinner
- ✅ Success check uses `colorScheme.primary` / `onPrimary` (no `0xFF4CAF50`)
- ✅ Motion budget is 150 + 100 + 50 ms; reduce-motion still skips immediately
- ✅ Success check sits at the FAB corner instead of a full-screen blocking overlay

## Tests

- Automated: Paparazzi `GroupListSkeletonPaparazziTest`; existing `ReduceMotionTest`

## Fallback validation

- Command: `./gradlew :app:testDebugUnitTest --tests com.multiappshare.GroupListSkeletonPaparazziTest`
