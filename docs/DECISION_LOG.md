# Decision log

Chronological index of major technical decisions. Entries are **append-only**.

| Date | Decision | Record |
|------|----------|--------|
| 2026-05-06 | Complete `:feature-dashboard` module (strategy A) rather than consolidate into `:app` | [ADR-0001](adr/0001-feature-dashboard.md) |
| 2026 | Milestones A–Q completed (testing, Room, UX, security, i18n) | [COMPLETED_TASKS.md](COMPLETED_TASKS.md) |
| 2026-06-12 | Milestone R — Project Initialization Template alignment complete | [COMPLETED_TASKS.md](COMPLETED_TASKS.md) |
| 2026-06-19 | Milestone U — Full agent-project-bootstrap v0.11.0 alignment (rules, 25 slash commands, gate scripts) | [`BOOTSTRAP_TEMPLATE_MAP.md`](BOOTSTRAP_TEMPLATE_MAP.md) |
| 2026-06-19 | V.3b — `targetSdk` 36 + `enableEdgeToEdge()` for API 36 / Android 16 compliance | [`TARGET_SDK_REVIEW.md`](TARGET_SDK_REVIEW.md) |
| 2026-06-19 | V.3b Android 16 smoke — CPH2583 pass (overlay, rotation, deeplinks, E2E insets) | [`GATES.md`](GATES.md) |
| 2026-06-20 | V.2 Dependabot triage — 0 Critical/High; merged #25, #23, #24; CI green on `8afb342` | [`GATES.md`](GATES.md) |
| 2026-06-19 | V.1 configuration cache re-validated under AGP 9.2.1 full gate suite | `gradle.properties` |
| 2026-07-12 | Audit X — require Android CI + CodeQL (not template Security Scan); keep Scorecard/Trivy optional; fix shell LF + Windows gh.exe | [`CODE_REVIEW.md`](../CODE_REVIEW.md) · [`SECURITY_TRIAGE.md`](SECURITY_TRIAGE.md) |
| 2026-07-12 | Automate X.4–X.6: Dependabot triage script, JAVA_HOME resolve, Trivy+Scorecard workflows (SHA-pinned trivy-action v0.35.0) | [SECURITY_TRIAGE.md](SECURITY_TRIAGE.md) |
| 2026-07-21 | Milestone Y — Align to agent-project-bootstrap v0.15.0 (skills/hooks/agents, TEMPLATE_INDEX, dependency-review); keep docs/ board paths + android.yml; no release-please | [BOOTSTRAP_ALIGNMENT.md](BOOTSTRAP_ALIGNMENT.md) |
| 2026-08-20 | Sync to agent-project-bootstrap **0.21.0** — coach/tour/ideas/codex-review, multi-agent adapters, `verify.sh`, `bootstrap.config.json`; preserve `android.yml`, `docs/` boards, `project.mdc` | [BOOTSTRAP_SYNC_0.21.md](BOOTSTRAP_SYNC_0.21.md) |
