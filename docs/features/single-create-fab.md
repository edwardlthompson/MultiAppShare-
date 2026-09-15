# Feature: single-create-fab

> One Create FAB; auto-group in overflow and empty state. BUILD_PLAN **UX-005**.

## Acceptance criteria

- ✅ Home shows one Create FAB when groups exist, with a contentDescription
- ✅ Empty state uses Auto-group as the filled primary and Create as text; no FABs
- ✅ Auto-group remains available from the overflow menu
- ✅ Group list has bottom content padding so the last row clears the FAB

## Tests

- Automated: `EmptyGroupsPlaceholderPaparazziTest` (re-record goldens)

## Fallback validation

- Command: `./gradlew :app:recordPaparazziDebug --tests com.multiappshare.EmptyGroupsPlaceholderPaparazziTest`
