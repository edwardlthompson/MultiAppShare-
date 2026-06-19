# Design guide — Multi App Share

> Bootstrap [`DESIGN_GUIDE`](https://github.com/edwardlthompson/agent-project-bootstrap/blob/main/docs/DESIGN_GUIDE.md) adapted for this Android project.

## UI stack

- **Jetpack Compose** + **Material 3** (dynamic color)
- Shared themes: `:core-ui` (`themes.xml`, colors)
- Compose theme: `app/.../ui/theme/` (`Theme.kt`, `Color.kt`, `Type.kt`)

## Feature structure

Follow [`GOLDEN_PATH.md`](GOLDEN_PATH.md): domain → repository → ViewModel → Compose UI → tests.

## i18n

- User strings in `app/src/main/res/values/strings.xml`
- Locales: `values-es/`, extend as needed
- `contentDescription` on interactive Compose nodes (accessibility)

## File limits

- View/composable files ≤ **250** lines
- Logic (ViewModel, repository impl, use case) ≤ **150** lines
- Enforced by `scripts/check-file-limits.ps1`

## Visual regression

- Paparazzi goldens under `app/src/test/snapshots/`
- Refresh: `./gradlew :app:recordPaparazziDebug` — see [`PAPARAZZI.md`](PAPARAZZI.md)

## Anti-patterns

- Business logic in `@Composable` or `MainActivity`
- Hardcoded English user strings
- Proprietary analytics or GMS UI components

See also [`MODULE_BOUNDARIES.md`](MODULE_BOUNDARIES.md) and [`ACCESSIBILITY_CHECKLIST.md`](ACCESSIBILITY_CHECKLIST.md).
