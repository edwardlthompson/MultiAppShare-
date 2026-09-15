# Feature: build-halt-local-gates

> Unblock `/build` when local environment — not product code — fails the gate loop, and when leftover 3-strike from a **prior** `/build` would skip autofix. BUILD_PLAN **BH-001–BH-007**.

## What halted `/build` (2026-09-15)

1. `pre-commit-hooks` — `check-pre-commit-hooks.sh` failed because `.git/hooks/commit-msg` was missing. `scripts/install-commit-msg-hook.sh` already exists and is used by `/setup` / init, but **feature-gate never calls it**.
2. `android-lint` — child repo has root `gradlew`, so `feature-gate.sh` ran Gradle **without** `android_sdk_ready`. `ANDROID_HOME` was unset; SDK was present at `~/Android/Sdk`. Template `examples/android` path already skips; **product path does not**.
3. **3-strike** — those two environment misses counted as code failures (`agent-progress.sh record` increments on any feature-gate exit ≠ 0). UX-001 never got a real lint/test pass.
4. **Leftover code halt** — after a real Detekt `ImportOrdering` 3-strike, `.cursor/agent-progress.json` kept `strikes: 3`. BH-004 `maybe-reset-env-strikes` does not clear that. The next `/build` then halted on the first miss **before autofix**.
5. **Double-count** — `watch-agent-gates --once --autofix` recorded a strike on the initial fail **and** on the autofix retry, so one compile/Detekt miss burned two strikes. `feature-autofix` also ran `pre-commit run trailing-whitespace end-of-file-fixer` (two hook IDs; unrecognized args).

## Acceptance criteria

- ✅ `check-pre-commit-hooks.sh` (or feature-gate preamble) runs `install-commit-msg-hook.sh` before failing
- ✅ Child Android gate calls `android_sdk_ready`; if false, write gitignored `local.properties` from well-known SDK dirs (`$ANDROID_HOME`, `$ANDROID_SDK_ROOT`, `$HOME/Android/Sdk`, Windows LocalAppData)
- ✅ Missing SDK after that is `block_env` (exit 2), not `android-lint` (exit 1)
- ✅ Environment / hook-install stages do not increment `strikes`
- ✅ A bootstrap check fails if 🔲 `[AGENT]` markdown rows are invisible to `build-sprint-status`
- ✅ `/build` Step 0 runs `maybe-reset-halt-strikes`: leftover `strikes >= 3` resets to 0 for any `failed_stage`. 3-strike still applies inside one `/build`. `watch-agent-gates` must **not** call this at start (that would allow a fourth try in the same run)
- ✅ Autofix retry in the same `watch-agent-gates --once` records with `--no-increment` (`FEATURE_GATE_SKIP_STRIKE=1`). Success still resets strikes to 0
- ✅ `feature-autofix.sh` runs `pre-commit run <hook-id> --files` once per hook (`trailing-whitespace`, then `end-of-file-fixer`)

## Tests

- Automated: unit/shell coverage for SDK path detection, hook install-before-fail, strike exemption for `failed_stage=environment`, halt reset at `strikes>=3`, `--no-increment` on retry
- Fallback: `bash scripts/verify-build-halt-local-gates.sh`
- Parser trap: never put the 4-column AGENT table after a heading matching `Parallel` (`### Parallelization` is swallowed as the Parallel lane)

## Fallback validation

- Command: `bash scripts/verify-build-halt-local-gates.sh`
