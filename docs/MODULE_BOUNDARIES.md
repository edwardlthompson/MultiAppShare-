# Module boundaries (Milestone **P.6**)

Ongoing discipline—not a one-time task.

- **`:core-domain`**: repositories, use cases, crypto/backup helpers, pure Kotlin logic testable without Compose.
- **`:core-database`**: Room entities, DAOs, `AppDatabase`, DI modules that construct DB; schema exports under **`core-database/schemas/`**.
- **`:feature-dashboard`**: dashboard feature UI + **`DashboardViewModel`** aligned with domain.
- **`:app`**: `MainActivity` orchestration, `MainScreen` / share overlay wiring, app-level ViewModels, navigation glue.

**Prefer** adding non-trivial logic in **domain** and injecting it—avoid growing **`MainActivity.kt`** / **`MainScreen.kt`** as catch-all files without extraction (**ADR-001** / **G.2**).
