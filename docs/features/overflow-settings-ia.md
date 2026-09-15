# Feature: overflow-settings-ia

> Overflow holds peer actions; Settings holds the rest. BUILD_PLAN **UX-007**.

## Acceptance criteria

- ✅ Overflow has ≤ 5 items: Sort, Auto-group, Share clipboard, History, Settings
- ✅ Clipboard uses ContentPaste; History uses History; Settings uses Settings
- ✅ Language, Theme, Delay, Export, Import, About open from Settings; Donate is on About

## Tests

- Automated: `OverflowMenuPolicyTest` (≤5 unique overflow icons; unique hub icons)
- Automated: `AppSettingsDialogPaparazziTest`
- Fallback: `./gradlew :core-domain:test :app:recordPaparazziDebug` then `:app:verifyPaparazziDebug`
