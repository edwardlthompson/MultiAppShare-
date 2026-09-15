# Feature: history-activity-ux

> History reads as recent activity: locale dates, semantic errors, empty CTA. BUILD_PLAN **UX-025**.

## Acceptance criteria

- ✅ Title is “Recent activity” (no “Last 50” capacity leak)
- ✅ Timestamps use locale `DateTimeFormatter` SHORT, not `MMM dd, HH:mm`
- ✅ Error rows use `colorScheme.error` plus an error icon and TalkBack `history_error_cd`
- ✅ Empty state includes “Share something to see it here.”
- ✅ Footer Close is back navigation; last re-share stays a TopAppBar `TextButton` (UX-018)
- ✅ en / es / fr / de / it for title, empty CTA, and error label

## Tests

- Automated: `HistoryPresentationTest`, Paparazzi empty + error row on `HistoryScreen`

## Fallback validation

- Command: `./gradlew :core-domain:test --tests com.multiappshare.domain.HistoryPresentationTest`
