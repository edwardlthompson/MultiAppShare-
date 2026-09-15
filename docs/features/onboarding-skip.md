# Feature: onboarding-skip

> Skip first-run onboarding in one tap. BUILD_PLAN **UX-002**.

## Acceptance criteria

- ✅ Skip is always visible on both onboarding pages
- ✅ Skip and system back on page 1 call `setOnboardingCompleted` (same as Set up Manually)
- ✅ Autofill remains the primary action on page 2
- ✅ en / es / fr strings for Skip

## Tests

- Automated: Paparazzi snapshot of `OnboardingDialog` showing Skip
- Instrumented helpers prefer Skip when dismissing startup dialogs

## Fallback validation

- Command: `./gradlew :app:testDebugUnitTest --tests com.multiappshare.OnboardingDialogPaparazziTest`
