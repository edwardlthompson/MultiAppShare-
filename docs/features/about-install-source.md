# Feature: about-install-source

> Show installation source (F-Droid vs Sideload / Direct APK) on About dialog. BUILD_PLAN **AE.53**.

## Acceptance criteria

- ✅ `InstallChannel.getSourceLabel` returns descriptive label for package installer source
- ✅ Correctly identifies F-Droid family installers, Google Play, and manual sideloads
- ✅ In-app About transparency

## Tests

- Automated: `InstallChannelTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
