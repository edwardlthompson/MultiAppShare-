# Branch protection setup (Milestone R.2.11)

Maintainer-only steps in GitHub **Settings → Branches → Branch protection rules** for `main`:

## Required status checks

Enable **Require status checks to pass before merging** and select:

| Check | Workflow |
|-------|----------|
| `build` | [`.github/workflows/android.yml`](../.github/workflows/android.yml) |
| `Analyze Kotlin/Java` (or `CodeQL`) | [`.github/workflows/codeql.yml`](../.github/workflows/codeql.yml) |

After the first successful PR run, exact check names appear in the GitHub UI—use those labels.

## Recommended options

- **Require linear history** (squash or rebase merges; no merge commits on `main`)
- **Require pull request reviews** (optional for solo maintainer)
- **Do not allow bypassing** (optional)

## Documented in

- [`CONTRIBUTING.md`](../CONTRIBUTING.md) — Git workflow
- [`AGENTS.md`](../AGENTS.md) — agent constraints

No in-repo automation can enable these rules; they require repository admin access.
