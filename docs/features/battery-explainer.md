# Feature: battery-explainer

> User-initiated battery-optimization copy and system settings intent link. BUILD_PLAN **AE.41**.

## Acceptance criteria

- ✅ `BatteryOptimizationExplainer.EXPLANATION` provides clear guidance on foreground service battery handling
- ✅ `BatteryOptimizationExplainer.getSettingsIntentAction` returns standard platform settings intent
- ✅ User-initiated only with zero automatic battery permission prompts

## Tests

- Automated: `BatteryOptimizationExplainerTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
