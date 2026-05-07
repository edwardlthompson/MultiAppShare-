# Room migration checklist (Milestone **P.5**)

When **`@Database` version** increments or entities change:

1. **Export schema**: KSP args in **`core-database/build.gradle.kts`** (`room.schemaLocation`) — commit new JSON under **`core-database/schemas/`**.
2. **Release path**: add explicit **`Migration`** objects (**F.2**); avoid relying on **`fallbackToDestructiveMigration()`** in release.
3. **Tests**: extend **`GroupsRepositoryTest`** / DAO tests for new columns or tables; add an instrumented migration test if the migration is non-trivial or destructive.
4. **Docs**: update **`docs/BACKUP_AND_CLOUD.md`** if backup JSON shape or restore semantics change.

If version stays **1** and schema unchanged, no action—this file is the trigger list for future bumps.
