# Feature: history-retention

> 30/90-day local history retention and automatic pruning. BUILD_PLAN **AE.26**.

## Acceptance criteria

- ✅ `HistoryRetentionPeriod` defines 30-day, 90-day, and indefinite history retention modes
- ✅ `HistoryPruner.prune` discards history items timestamped before the cutoff date
- ✅ All history processing stays strictly on-device

## Tests

- Automated: `HistoryPrunerTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
