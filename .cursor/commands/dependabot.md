# Dependabot triage

Follow @docs/SECURITY_TRIAGE.md and @KNOWLEDGE_BASE.md KB-007.

```bash
bash scripts/triage-dependabot-prs.sh           # report
bash scripts/triage-dependabot-prs.sh --apply   # squash-merge green+MERGEABLE Dependabot PRs
```

Prioritize Critical/High alerts first (`scripts/count-critical-high-dependabot.sh`).
Document temporary overrides in @DECISION_LOG.md.

Begin now.
