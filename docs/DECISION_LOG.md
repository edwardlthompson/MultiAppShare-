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
| 2026-08-20 | **AC** — optional once-daily GitHub Releases check + quiet Venmo donate (Continuum Calendar method). `INTERNET` is for `releases/latest` only; donate/update prefs stay device-local | [`docs/features/donations-and-updates.md`](features/donations-and-updates.md) |
| 2026-08-30 | Template catch-up **0.21.0 → 1.0.0** — Canon commands/help/scripts + Mixed workflows; Sacred `AGENTS.md` and product modules untouched; no `examples/` overwrite | [BOOTSTRAP_ALIGNMENT.md](BOOTSTRAP_ALIGNMENT.md) |
| 2026-08-30 | **AD** Golden Path 1–7 in existing hosts (About, theme, crash queue, sanitized GitHub feedback, display-refresh). Crash/refresh prefs omitted from backup export. Local `feature-gate --stack android` passed | [COMPLETED_TASKS.md](COMPLETED_TASKS.md) |
| 2026-08-30 | **AE** — Completed 70 vertical slice product backlog rows (AE.1–AE.70); automated human leftovers; verified ADB instrumented tests on hardware; prepared and shipped v1.10.0 | [COMPLETED_TASKS.md](COMPLETED_TASKS.md) |
