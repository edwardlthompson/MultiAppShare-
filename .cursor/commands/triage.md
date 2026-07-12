# Weekly security triage

Follow @docs/SECURITY_TRIAGE.md weekly pass.

```bash
bash scripts/check-security-triage.sh --wait-ci 300
bash scripts/triage-dependabot-prs.sh
bash scripts/enable-optional-security-workflows.sh   # status; --apply to add Trivy/Scorecard
```

Confirm **Android CI**, **CodeQL**, and (if present) **Security Scan** green via `bash scripts/check-github-ci.sh --wait 300`.

Begin now.
