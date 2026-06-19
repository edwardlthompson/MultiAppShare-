# Local validation gates — Multi App Share

Run pre-push validation (Git Bash on Windows):

```bash
bash scripts/validate-bootstrap.sh --quick
./gradlew lint test detekt koverXmlReport assembleDebug
bash scripts/feature-gate.sh --stack android
bash scripts/check-repo-hygiene.sh
```

Report pass/fail per script. Fix failures before marking BUILD_PLAN items complete.

Begin now.
