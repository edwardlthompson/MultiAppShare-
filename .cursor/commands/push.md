# Release push — Multi App Share

**Explicit push approval granted** by invoking this command.

1. Confirm [`docs/PRE_RELEASE_AUDIT.md`](docs/PRE_RELEASE_AUDIT.md) complete.
2. Bump `versionCode` / `versionName` in `app/build.gradle.kts`, F-Droid metadata, fastlane changelogs, `CHANGELOG.md`.
3. Run `/prerelease` gates locally.
4. Commit with Conventional Commits message (e.g. `chore(release): v1.x.x`).
5. Push and create tag per [`FDROID_MAINTENANCE.md`](FDROID_MAINTENANCE.md).

```bash
bash scripts/pre-release-gate.sh
git push origin main
git push origin vX.Y.Z
```

Never commit signing secrets (`keystore.properties`, keystores).

Begin now.
