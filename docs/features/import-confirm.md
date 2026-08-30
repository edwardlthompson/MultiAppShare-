# Feature: import-confirm

> Confirm before destructive backup import (Replace mode with existing groups). BUILD_PLAN **AE.63**.

## Acceptance criteria

- ✅ Domain logic checks whether confirmation prompt is necessary based on existing group count and selected import strategy (Replace vs Merge)
- ✅ Clear warning formatting indicating how many existing groups will be overwritten
- ✅ Unit tests verify singular/plural message formatting and conditional prompting

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
