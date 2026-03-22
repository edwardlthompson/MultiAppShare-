# Contributing to Multi App Share

Thank you for considering contributing to Multi App Share! To maintain code quality, rigid encapsulation, and FOSS compliance, please adhere to these guidelines layout triggers down downstream.

---

## 🏗 Architectural "Golden Rules"

To uphold a Clean Architecture layout, candidates must adhere to:

1. **Inward Dependency Flow**: 
   - `:feature-*` depends on `:core-domain` & `:core-ui`.
   - `:core-database` implements `:core-domain` interfaces.
   - `:core-domain` depends on **nothing** (Pure Kotlin only).
2. **Strict Encapsulation**: 
   - Use `internal` modifiers for concretions (e.g., Room database triggers, implementation bounds). Only expose interfaces/entities publicly list.
3. **Context Safety**: 
   - No `Activity` or `Fragment` context leaks inside ViewModels or Repositories. Use `Hilt` for explicit DI bindings layout setup!

---

## ✅ Pull Request (PR) Checklist

Before submitting a PR, ensure:
- [ ] **Unit Tests**: Pass for any edits to `:core-domain` or `:core-database` node logic layout.
- [ ] **Lint Pass**: Android Lint completes without **Warnings** layout disputes.
- [ ] **No Proprietary Bloat**: Zero imports from `com.google.android.gms` or Firebase branches.

---

## 🛡 FOSS Compliance

This is a **FOSS-first** project:
- Additions must adhere to **MIT / Apache 2.0** permissive licenses.
- No inclusion of binary blobs or non-free tracking SDKs layout.

---

## 🌳 Git Workflow

- **Commits**: Use **Conventional Commits** (e.g., `feat:`, `fix:`, `docs:`, `chore:`).
- **Branching**: Branch off `develop` (or a feature node) before targeting `main` releases layout.
