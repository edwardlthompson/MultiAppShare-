# ADR-0001: Feature dashboard module vs consolidating in `:app`

## Status

Accepted (2026-05-06)

## Context

The project includes `:feature-dashboard` with `DashboardViewModel`, while `MainActivity` and the primary UX use `MainViewModel`. `DashboardViewModel.loadData()` is incomplete (does not publish `DashboardUiState.Success`), so the module is not yet the single source of dashboard truth.

## Decision

Choose **strategy A**: **complete** `:feature-dashboard`—finish `DashboardViewModel.loadData()` so it mirrors `MainViewModel`'s data-loading behavior (groups, resolved apps, history), expose `DashboardUiState`, then **migrate** dashboard-related composables off `:app` incrementally.

**Strategy B** (delete or hollow out `:feature-dashboard` and keep everything in `:app`) is **deferred**: the module is already a Gradle dependency of `:app`, and consolidation would not reduce complexity until `MainActivity` is split (**G.2**); removing the module now would churn imports without delivering modular boundaries.

## Consequences

- **Positive:** Clear path to **G.2** (smaller `MainActivity`) without duplicating repository wiring.
- **Negative:** Short-term duplicate ViewModel surface until migration finishes; must guard against divergent behavior by reusing the same repositories/use cases (already injected in both models).

## Compliance

Track progress under **BUILD_PLAN** milestones **G.1** and **G.2**. Future work: Milestone **S**.
