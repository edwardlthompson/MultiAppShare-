# Feature: i18n-domain-labels

> User-visible copy lives in `strings.xml`, not domain helpers. BUILD_PLAN **UX-022**.

## Acceptance criteria

- ✅ `SettingsCatalog` titles are search ids, not English labels; UI maps ids to resources
- ✅ `EmptyGroupsCtaHelper` no longer returns English action copy
- ✅ `MimeMismatchExplanation.mimeKind` returns keys (`image` / `video` / `text` / `other`)
- ✅ `BatteryOptimizationExplainer` exposes only the settings intent action
- ✅ `HomeWidgetModel.createWidgetState` takes `fallbackLabel` from `R.string.widget_recent_group`
- ✅ `GroupNameHelper.uniqueCopyName` takes `copyLabel` from `R.string.group_copy_label`
- ✅ en / es / fr / de / it include `group_copy_label` and `widget_recent_group`

## Tests

- Automated: `SettingsCatalogTest`, `GroupNameHelperTest`, `HomeWidgetModelTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
