# Feature: foss-funding-links

> Direct HTTPS funding links for Liberapay and GitHub Sponsors alongside Venmo. BUILD_PLAN **AE.59**.

## Acceptance criteria

- ✅ `AboutLinks.LIBERAPAY` and `AboutLinks.GITHUB_SPONSORS` supply valid HTTPS donation URLs
- ✅ Completely user-initiated (`ACTION_VIEW` browser launch only)
- ✅ Never interleaved into update alerts or critical flows

## Tests

- Automated: `AboutLinksTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
