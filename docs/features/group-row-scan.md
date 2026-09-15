# Feature: group-row-scan

> Group rows show app count and last-shared recency without expanding. BUILD_PLAN **UX-019**.

## Acceptance criteria

- Group title uses `titleMedium`
- Subtitle is “N apps · last shared …” (or Never shared) without expanding
- Empty groups show a primary **Add apps** action that opens modify apps

## Tests

- Automated: `GroupRowMetaTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
