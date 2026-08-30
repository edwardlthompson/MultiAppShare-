# Feature: group-last-share-time

> Show human-readable relative last-share timestamp on group rows. BUILD_PLAN **AE.69**.

## Acceptance criteria

- ✅ `GroupLastShareTimeHelper` formats relative timestamps (Just now, Xm ago, Xh ago, Xd ago, Never shared)
- ✅ Handles null, zero, and future clock drift gracefully
- ✅ Unit tests verify standard relative time formatting brackets

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
