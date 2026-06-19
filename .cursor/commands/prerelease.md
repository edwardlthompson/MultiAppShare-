# Pre-release gate — Multi App Share

Follow [`docs/PRE_RELEASE_AUDIT.md`](docs/PRE_RELEASE_AUDIT.md) and [`docs/RELEASE_SMOKE.md`](docs/RELEASE_SMOKE.md).

```bash
bash scripts/pre-release-gate.sh
./gradlew lint test detekt koverXmlReport assembleDebug
./gradlew :app:verifyPaparazziDebug
```

When `[ADB]` device connected, also run:

```bash
./gradlew :app:connectedDebugAndroidTest
```

F-Droid metadata: [`FDROID_MAINTENANCE.md`](FDROID_MAINTENANCE.md). Signed release: [`docs/LOCAL_RELEASE_BUILD.md`](docs/LOCAL_RELEASE_BUILD.md).

Do not tag or `/push` until gates pass. Record evidence in [`docs/GATES.md`](docs/GATES.md).

Begin now.
