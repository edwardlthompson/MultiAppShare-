# Feature: paparazzi-ad-dialogs

> Paparazzi snapshot golden testing for Milestone AE dialogs and search inputs. BUILD_PLAN **AE.56**.

## Acceptance criteria

- ✅ `MilestoneAePaparazziTest` verifies UI layout fidelity on Pixel 5 device profile
- ✅ Local host-driven UI regression prevention

## Tests

- Automated: `MilestoneAePaparazziTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
