# Runbook — Multi App Share

> Operational guide for release, rollback, and agent gate failures.

## Local gates (before push)

```bash
bash scripts/validate-bootstrap.sh --quick
./gradlew lint test detekt koverXmlReport assembleDebug
bash scripts/feature-gate.sh --stack android
```

**Windows Git Bash:** set Java before bash gates:

```bash
export JAVA_HOME="/c/Program Files/Android/Android Studio/jbr"
bash scripts/watch-agent-gates.sh --once --autofix
```

## Release

1. `[AUTO]` CI green on `main` (`Android CI`, `CodeQL`)
2. `[AGENT]` `/prerelease` or `docs/PRE_RELEASE_AUDIT.md`
3. `[ADB]` `[HUMAN]` `docs/RELEASE_SMOKE.md` on device
4. `[HUMAN]` Tag + F-Droid — [`FDROID_MAINTENANCE.md`](../FDROID_MAINTENANCE.md)

## CI log fetch

```powershell
.\scripts\get-ci-logs.ps1
```

Requires `scripts/.env.local` with `GITHUB_TOKEN`.

## Common Failures

| Symptom | Check | Fix |
|---------|-------|-----|
| CI failing on lint | `./gradlew lint` locally | Fix and push |
| `JAVA_HOME not set` in Git Bash | Android Studio JBR path | Export `JAVA_HOME` (see above) |
| Paparazzi diff | `./gradlew :app:verifyPaparazziDebug` | Record goldens if intentional |
| F-Droid metadata | `FDROID_MAINTENANCE.md` | Bump versionCode + changelog txt |

## Rollback

1. Revert to previous release tag
2. Re-run smoke on device
3. Log in `docs/DECISION_LOG.md` if user-impacting

---

## Generic template sections (N/A for this Android app)
| Dependabot alert | `docs/SECURITY_TRIAGE.md` | Merge bump PR |
| State lost after upgrade | Migration tests | Fix schema migration |

## Backup & Restore

| Target | RPO | RTO | Procedure |
|--------|-----|-----|-----------|
| User data | _Define_ | _Define_ | _Document per stack_ |
| Repository | N/A (git) | Immediate | `git clone` |

## SLOs (`[HUMAN]` defines)

| Service | SLI | Target |
|---------|-----|--------|
| _Example: API availability_ | Uptime | _99.9%_ |
| _Example: page load_ | p95 latency | _< 2s_ |

## Escalation

1. Check `docs/BUILD_PLAN.md` Ongoing Maintenance
2. Review `docs/SECURITY_TRIAGE.md` for security issues
3. Contact maintainers in `.github/CODEOWNERS`

## Secret Rotation

When credentials leak or a team member with access leaves:

1. **`[HUMAN]`** Revoke compromised tokens/keys in the provider console immediately
2. **`[AGENT]`** Rotate secrets in GitHub Environments and local `.env` (never commit)
3. **`[AGENT]`** Update `.env.example` if variable names changed
4. **`[AUTO]`** Re-run CI with new secrets; confirm deploy health checks pass
5. **`[HUMAN]`** Log incident in `DECISION_LOG.md`; link advisory if CVE-related
