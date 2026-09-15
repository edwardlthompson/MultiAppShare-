# Feature: microcopy-rewrites

> Calm product copy: brand spacing, seconds, Merge verb, no bang or ⋮. BUILD_PLAN **UX-031**.

## Acceptance criteria

- ✅ Onboarding welcomes “Multi App Share”
- ✅ History title already “Recent activity” (UX-025)
- ✅ Delay presets speak seconds, not milliseconds
- ✅ Empty-group hint is “Add apps to this group”
- ✅ Expand/collapse TalkBack already state-aware (UX-029)
- ✅ Merge confirm is `button_merge`, not Save
- ✅ Skip remaining apps vs Skip this app
- ✅ Sharing headline has no bang; overlay copy has no ⋮

## Tests

- Automated: Paparazzi `OnboardingDialogPaparazziTest` (welcome title)

## Fallback validation

- Command: `./gradlew :app:recordPaparazziDebug --tests com.multiappshare.OnboardingDialogPaparazziTest`
