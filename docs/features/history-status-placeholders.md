# Feature: history-status-placeholders

> Semantic history status colors and letter avatars instead of hash RGB. BUILD_PLAN **UX-028**.

## Acceptance criteria

- ✅ History error rows keep `colorScheme.error` plus icon (UX-025); pending/success status uses `onSurfaceVariant`
- ✅ Leftover `HistoryDialog` in `MainActivityDialogs` is deleted
- ✅ App list placeholders are `surfaceVariant` circles with the first letter, not hash-to-RGB
- ✅ `AppAvatarPlaceholder.initial` covers blank names

## Tests

- Automated: `AppAvatarPlaceholderTest`

## Fallback validation

- Command: `./gradlew :core-domain:test --tests com.multiappshare.domain.AppAvatarPlaceholderTest`
