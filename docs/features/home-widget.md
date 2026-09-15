# Feature: home-widget

> Launcher home-screen quick-share widget. BUILD_PLAN **AE.45**.
>
> Layer: user-visible (`HomeShareWidget`). Domain: `HomeWidgetModel`.

## Acceptance criteria

- ✅ User-visible: `HomeShareWidget` AppWidget with `widget_home_share` layout
- ✅ Domain: `HomeWidgetModel.createWidgetState` computes display state for widget UI
- ✅ Falls back to clean defaults when no groups exist
- ✅ 100% offline state

## Tests

- Automated: `HomeWidgetModelTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
