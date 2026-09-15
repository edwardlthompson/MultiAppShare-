# Feature: group-filter-clear

> Trailing clear, share-mode search, and saveable query. BUILD_PLAN **UX-026**.

## Acceptance criteria

- ✅ Filter label is not duplicated as a placeholder
- ✅ Trailing clear is visible when the query is non-empty
- ✅ Filter-empty state has a Clear search CTA
- ✅ Search stays available in share mode and filters MIME-compatible groups
- ✅ `groupFilterQuery` uses `rememberSaveable` (dialog saveable remains UX-069)

## Tests

- Automated: `GroupListFilterTest`, Paparazzi `MilestoneAePaparazziTest.groupFilterField_activeSearch`

## Fallback validation

- Command: `./gradlew :core-domain:test --tests com.multiappshare.domain.GroupListFilterTest`
