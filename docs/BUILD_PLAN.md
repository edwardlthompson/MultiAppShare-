# MultiAppShare BUILD PLAN – Living Checklist

Completed milestones **A–T** are archived in [`docs/COMPLETED_TASKS.md`](COMPLETED_TASKS.md).

Agent workspace: [`AGENTS.md`](../AGENTS.md), [`AGENT_MEMORY.md`](../AGENT_MEMORY.md), [`KNOWLEDGE_BASE.md`](../KNOWLEDGE_BASE.md).

**Bootstrap reference:** [`edwardlthompson/agent-project-bootstrap`](https://github.com/edwardlthompson/agent-project-bootstrap) **v0.11.0** · sibling pattern: **Screen Wakelock Detector** (existing-repo android child).

---

## Owner label legend (bootstrap alignment)

| Label | Owner | When to use |
|-------|-------|-------------|
| `[AGENT]` | Cursor Agent | Code, docs, scaffolding, tests, CI config |
| `[HUMAN]` | Human developer | Approvals, credentials, GitHub settings, product decisions |
| `[ADB]` | Human (Android) | Emulator/device testing, F-Droid submission |
| `[AUTO]` | CI/scripts/bots | GitHub Actions, Dependabot, pre-commit |

**Agent rule:** Execute all `[AGENT]` **Sequential** items first, then dispatch **Parallel** agents with isolated file scopes. After each major `[AGENT]` step run `bash scripts/watch-agent-gates.sh --once --autofix`.

Filter: `grep '\[AGENT\]' docs/BUILD_PLAN.md`

> **Note:** Milestones A–T used mixed `[Agent]` labels. From Milestone **U** onward, use uppercase `[AGENT]` / `[HUMAN]` per bootstrap convention.

---

## Roadmap process — milestone gates (mandatory)

Before closing a milestone and starting the next:

1. **Automated gate**: `./gradlew lint test detekt koverXmlReport assembleDebug` — all **pass**.
2. **File size gate**: `bash scripts/check-file-limits.sh` (migrate from `check-file-limits.ps1`; keep PS1 wrapper if needed)
3. **Bootstrap gate** (from Milestone U): `bash scripts/validate-bootstrap.sh --quick`
4. **Manual smoke** when UX is touched: device/emulator pass; note under Evidence.
5. **Regression**: no untriaged **P1** defects from that milestone's scope.

**Product scope:** Phones and tablets only. Wear OS and Android TV are out of scope.

---

## Pre-release gate (each `v*` tag)

Required **before every release** per [`docs/PRE_RELEASE_AUDIT.md`](PRE_RELEASE_AUDIT.md). Milestone **R.5.2** and **S.9.2** first passes archived in [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md).

| Item | Owner | When |
|------|-------|------|
| **R.5.1** Automated Gradle/Paparazzi/metadata | `[AGENT]` / CI | Every tag |
| **R.5.2** Release smoke on device | `[ADB]` `[HUMAN]` | Every tag — see [`RELEASE_SMOKE.md`](RELEASE_SMOKE.md) |
| **R.5.3** TalkBack / 200% font | `[HUMAN]` | When UX changed |
| **R.5.4** Tag + F-Droid + GitHub Release | `[HUMAN]` | Every tag |

Post–Milestone U, prefer slash command `/ship` (prerelease → push → regress) or `/prerelease` for the automated portion.

---

## Milestone W — Post-U Audit (COMPLETE)

> **`/audit` 2026-06-19** · All items closed 2026-06-19

- ✅ Pinned GitHub Actions SHAs (CodeQL F-003)
- ✅ `docs/BUILD_PLAN.md` path normalization; `check-parallel-scope.sh`; session-state example
- ✅ Commit bootstrap stack (F-001) — `chore(agent): bootstrap v0.11.0 alignment`
- ✅ Dependabot vulnerability alerts + security updates enabled via `gh api` (F-002)
- ✅ Slash commands verified — `verify-slash-commands.sh` 25/25 + `batch-commands.mdc` (F-007, `[AUTO]`)

---

## Milestone U — Template Migration Sprint (COMPLETE)

> **Goal:** Fully align this existing Android repo with **agent-project-bootstrap v0.11.0**. **Completed 2026-06-19.**

> **Evidence:** `validate-bootstrap.sh --quick` ✅ · `verify-slash-commands.sh` ✅ · `./gradlew lint test detekt koverXmlReport assembleDebug` ✅ · Gradle via Android Studio JBR. Git Bash gate loop requires `JAVA_HOME` (see [`docs/GATES.md`](GATES.md)).

### Assessment summary (2026-06-19)

_Archived — see [`docs/COMPLETED_TASKS.md`](COMPLETED_TASKS.md) Milestone U when appended._

### U.1 — Unblock version control ✅

- `.cursor/` tracked; ephemeral files ignored
- `.cursorignore`, `.template-version` (0.11.0), `.cursor/stack-selection.json`

### U.2 — Bootstrap scripts ✅

- 30+ gate scripts merged; `feature-gate.sh` includes detekt + Kover
- `validate-bootstrap.sh` android child profile for `docs/BUILD_PLAN.md`

### U.3 — Cursor rules ✅

- 12 `.mdc` rules + `project.mdc` (retired `multiappshare.mdc`)

### U.4 — Slash commands ✅

- 25 `.cursor/commands/*.md`; customized `gates`, `init`, `prerelease`, `regress`, `ci`, `push`

### U.5 — Documentation ✅

- `START_HERE`, `CURSOR_MODES`, `FOR_AGENTS`, `BOOTSTRAP_TEMPLATE_MAP`, `DESIGN_GUIDE`, `GATES`, `PRIVACY`
- Updated `AGENTS.md`, `AGENT_MEMORY.md`, `README.md`, `DECISION_LOG.md`

### U.6 — Validation ✅ (agent) / `[HUMAN]` slash menu test pending

| Check | Status |
|-------|--------|
| `validate-bootstrap.sh --quick` | ✅ |
| `verify-slash-commands.sh` | ✅ |
| `./gradlew lint test detekt koverXmlReport assembleDebug` | ✅ |
| `watch-agent-gates.sh --once` in Git Bash | ⚠️ Needs `JAVA_HOME` |
| Cursor `/` menu visibility | `[HUMAN]` |

---

## Deferred milestones (after U)

Pick one when planning post-migration work:

- **V** — Re-enable Gradle configuration cache (`org.gradle.configuration-cache=true`) after AGP 9 validation (**E.5**).
- **V** — Dependabot alerts + security advisory triage (**T.0.1**, `[HUMAN]`).
- **V** — `targetSdk` / platform behavior review when raising beyond **35** — see [`TARGET_SDK_REVIEW.md`](TARGET_SDK_REVIEW.md).

Toolchain after **Milestone T** (2026-06-13): Gradle **9.5.1**, AGP **9.2.1**, Kotlin **2.4.0**, `compileSdk` **37** — details in [`COMPLETED_TASKS.md`](COMPLETED_TASKS.md#milestone-t--dependabot-dependency-upgrades).
