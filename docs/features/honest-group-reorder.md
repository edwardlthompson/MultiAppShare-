# Feature: honest-group-reorder

> Remove the non-dragging handle; expose Sort Groups as a text action. BUILD_PLAN **UX-006**.

## Acceptance criteria

- ✅ `GroupItem` has no `DragHandle` icon
- ✅ Home list with 2+ groups shows a Sort Groups text button that opens the existing sort dialog

## Tests

- Automated: compile + existing unit tests (Paparazzi list goldens if any)

## Fallback validation

- Command: `./gradlew :app:testDebugUnitTest`
