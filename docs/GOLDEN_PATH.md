# Golden path — feature template

Use **encrypted backup round-trip** as the structural template for new features.

## Layers (outside → in)

| Layer | Example in backup feature |
|-------|---------------------------|
| Pure logic | `core-domain/.../BackupCipher.kt` — AES-GCM, no Android APIs |
| Repository | `GroupsRepository` / `HistoryRepository` — JSON serialize, Room |
| ViewModel | `MainViewModel` — export/import orchestration, toasts |
| UI | `BackupDialogs.kt` — passphrase dialogs, SAF URIs |
| Tests | `BackupCipherTest`, `GroupsRepositoryTest`, instrumented smoke |

## Checklist for a new feature

1. **Domain first** — pure Kotlin in `:core-domain` with unit tests.
2. **Repository boundary** — persist via Room or DataStore; no Compose in repositories.
3. **ViewModel** — expose `StateFlow` / events; inject use cases via Hilt.
4. **Compose UI** — string resources in `values/strings.xml` (+ fr/es); `contentDescription` on interactive nodes.
5. **Tests** — unit tests for logic; Paparazzi for stable Composables; manual/adb checklist for OEM-heavy flows.
6. **Docs** — update BUILD_PLAN or KNOWLEDGE_BASE if non-obvious edge cases exist.

## Anti-patterns

- Business logic in `@Composable` functions or `MainActivity`
- Direct `SharedPreferences` (use DataStore)
- Proprietary crypto or analytics SDKs
- Hardcoded English user strings

See [`MODULE_BOUNDARIES.md`](MODULE_BOUNDARIES.md) and [`AGENTS.md`](../AGENTS.md).
