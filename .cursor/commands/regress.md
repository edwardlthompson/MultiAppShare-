# Post-release regression — Multi App Share

After a `v*` tag or release candidate:

```bash
./gradlew :app:verifyPaparazziDebug
./gradlew lint test detekt koverXmlReport assembleDebug
bash scripts/validate-bootstrap.sh --quick
```

With device/emulator (`[ADB]`):

```bash
./gradlew :app:connectedDebugAndroidTest
```

Manual checklist when UX changed: [`docs/MANUAL_SHARE_CHECKLIST.md`](docs/MANUAL_SHARE_CHECKLIST.md), [`docs/ACCESSIBILITY_CHECKLIST.md`](docs/ACCESSIBILITY_CHECKLIST.md).

Optional CI poll: `bash scripts/check-github-ci.sh HEAD --wait 300`

Begin now.
