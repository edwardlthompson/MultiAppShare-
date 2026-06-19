# CI poll — Multi App Share

Poll GitHub Actions for the current commit (requires `gh` auth):

```bash
bash scripts/check-github-ci.sh HEAD --wait 300
```

Primary workflows: **Android CI** (`.github/workflows/android.yml`), **CodeQL**.

For local CI log fetch: `pwsh scripts/get-ci-logs.ps1` (see `scripts/.env.local.example`).

Report workflow URL and conclusion for each required check.

Begin now.
