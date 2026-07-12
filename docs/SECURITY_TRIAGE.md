# Security Triage

Weekly CVE triage playbook for Dependabot alerts and release security gates.

## This repo (reference mode)

Required GitHub workflow names (what `scripts/check-github-ci.sh` polls when `.github/workflows/android.yml` is present):

| Workflow | File | Role |
|----------|------|------|
| **Android CI** | `.github/workflows/android.yml` | Build, test, hygiene, feature gate |
| **CodeQL** | `.github/workflows/codeql.yml` | Actions YAML analysis (Kotlin app scan deferred until CodeQL supports Kotlin 2.4.x) |
| **Security Scan** | `.github/workflows/security.yml` | Trivy fs scan (enable via `scripts/enable-optional-security-workflows.sh --apply`) |
| OpenSSF Scorecard | `.github/workflows/scorecard.yml` | Scorecard SARIF (same enable script) |
| PR title check | `.github/workflows/pr-title.yml` | Conventional PR titles (not a release blocker) |

`check-security-triage.sh` SKIPs Scorecard when `scorecard.yml` is absent. `check-github-ci.sh` adds **Security Scan** when `security.yml` exists.

Override required workflow names with `GITHUB_REQUIRED_WORKFLOWS` if needed.

## Setup (one-time, [HUMAN])

1. Open GitHub → **Settings** → **Code security and analysis**
2. Enable **Dependabot alerts** and **Dependabot security updates**
3. Enable **Private vulnerability reporting**
4. Verify `.github/dependabot.yml` exists
5. Configure branch protection on `main` requiring at least **Android CI** and **CodeQL** (add **Security Scan** after enabling Trivy). Prefer **Settings → Rules → Rulesets** when classic branch protection returns `404`.
6. Optional: `bash scripts/enable-optional-security-workflows.sh --apply`

`dependabot.yml` schedules version-update PRs; **Dependabot alerts** are a separate setting for CVE advisories — both are required.

## Weekly Triage Pass

Recommended cadence: **Monday**.

| Step | Owner | Action |
|------|-------|--------|
| 1 | HUMAN/AUTO | Open **Security → Dependabot alerts**; sort Critical/High first |
| 2 | AUTO | `bash scripts/triage-dependabot-prs.sh` (report) or `--apply` (squash-merge green PRs) |
| 3 | AGENT | Resolve conflicted Dependabot PRs / recreate via `@dependabot recreate` |
| 4 | AUTO | **Android CI** + **CodeQL** (+ **Security Scan** if enabled) validate merges |
| 5 | AUTO | `bash scripts/check-security-triage.sh --wait-ci 300` |

On Windows Git Bash, scripts resolve `gh.exe` via `scripts/lib/resolve-gh.sh` and JDK via `scripts/lib/resolve-java-home.sh`.

## OpenSSF Scorecard

- Enable: `bash scripts/enable-optional-security-workflows.sh --apply`
- Workflow: `.github/workflows/scorecard.yml` (`name: OpenSSF Scorecard`)
- Weekly triage: `check-security-triage.sh` reports the latest run; `--strict` fails on missing/failed Scorecard when the workflow file exists

## Triage Decisions

| Decision | When | Action |
|----------|------|--------|
| **Fix** | Patch available, low risk | `triage-dependabot-prs.sh --apply` or [AGENT] applies bump |
| **Defer** | No fix yet, acceptable risk window | Open issue with expiry date; log in DECISION_LOG.md |
| **Dismiss** | False positive or not applicable | Document rationale in issue or ADR |

After triage, confirm required workflows are green on `main`.

## GitHub Actions Pin Policy

Third-party workflow actions should use **immutable refs**:

| Rule | Detail |
|------|--------|
| **Allowed** | `@vX.Y.Z` or full commit SHA with `# vX.Y.Z` comment |
| **Forbidden** | Bare semver (`@0.28.0`), floating `@v0` / `@main`, unpinned third-party actions |
| **Trivy** | Pin `aquasecurity/trivy-action` to **v0.35.0** commit SHA only (post March 2026 supply-chain incident) |
| **Post-push** | `scripts/check-github-ci.sh --wait 300` |

## Release Gate (mandatory before tag)

Before any version bump or GitHub Release:

- Weekly triage completed within last **7 days**
- Zero open **Critical/High** Dependabot alerts (or documented exception with [HUMAN] approval)
- Deferred vulnerabilities have a linked issue and [HUMAN] sign-off
- Required workflows green on `main` (`bash scripts/check-github-ci.sh --wait 300`)
- Local: `bash scripts/pre-release-gate.sh` (or `/prerelease` / `/ship`)

If a Critical/High alert has no upstream fix, release may proceed only when:

1. A linked issue documents the advisory, impact, and mitigation
2. [HUMAN] explicitly approves in the release notes or DECISION_LOG.md

## Related Files

| File | Purpose |
|------|---------|
| `.github/dependabot.yml` | Weekly grouped version-update PRs |
| `.github/workflows/android.yml` | Android CI |
| `.github/workflows/codeql.yml` | CodeQL (Actions-only until Kotlin 2.4 supported) |
| `.github/workflows/security.yml` | Trivy Security Scan (optional enable script) |
| `.github/workflows/scorecard.yml` | OpenSSF Scorecard (optional enable script) |
| `scripts/triage-dependabot-prs.sh` | Report / squash-merge green Dependabot PRs |
| `scripts/enable-optional-security-workflows.sh` | Create Trivy + Scorecard workflows |
| `scripts/lib/resolve-java-home.sh` | Auto-detect JDK for Android gates |
| `scripts/check-security-triage.sh` | Weekly Dependabot + workflow gate |
| `scripts/check-github-ci.sh` | Poll required workflows on a commit |
| `scripts/lib/resolve-gh.sh` | Windows-friendly `gh` / `gh.exe` resolution |
| `scripts/pre-release-gate.sh` | Pre-release dry-run |
| `docs/PRE_RELEASE_AUDIT.md` | Human pre-release checklist |
