# Profiling & measurement (Milestone **P.4**)

Run **after** a baseline profile is shipped (**I.3** / **P.1**) so optimizations target real startup and handoff costs—not guesses.

## Cold start & main frame

1. **Android Studio Profiler** → CPU / Memory while cold-launching the app (force-stop first).
2. Optionally **`./gradlew :app:generateBaselineProfile`** (device) to refresh **`baseline-prof.txt`** when flows change materially.

## Sequential share handoff

1. Profiler **CPU** timeline during **ACTION_SEND** → overlay → **Next** → target app → return.
2. **`./gradlew :baselineprofile:connectedBenchmarkReleaseAndroidTest`** only if you extend **`BaselineProfileGenerator`** for share paths (optional; heavier).

## Macrobenchmark (optional)

Use **`:baselineprofile`** / **`benchmark-macro-junit4`** tests on a **non-debug** variant when comparing before/after refactors. Pair with **`docs/BASELINE_PROFILE.md`** for signing/install constraints.

Record conclusions in PR descriptions or a short note here—screenshots optional.
