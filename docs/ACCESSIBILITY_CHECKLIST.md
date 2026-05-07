# Accessibility checklist (share and groups)

Use this for **K.4** / **L.13** regression passes and **Milestone Q.1** in **`docs/BUILD_PLAN.md`**. Re-run after major UI changes to `MainScreen`, `MainActivityGroupUi`, or dialogs.

## TalkBack / screen reader

- [USER-SKIP] **Main screen**: Top app bar menu opens; each **DropdownMenuItem** is focusable and announces label.
- [USER-SKIP] **Group rows**: Expand/collapse control has a clear **content description**; **Drag handle** icon announces **share order** hint; **More** menu lists modify / reorder / shortcut / delete.
- [USER-SKIP] **Share overlay**: **Choose a group** card is read before the list; each group card is focusable in share mode.
- [USER-SKIP] **Sequential overlay**: **Next App** / **Finish**, **Replay**, **Previous** labels are distinct; step and **Next: {app}** are readable in order.
- [USER-SKIP] **Search groups** (when more than 8 groups): field has **content description**; empty filter state announces **no_groups_match_filter**.

## Display size and font scale (~200%)

- [USER-SKIP] **Groups** title in **TopAppBar** does not clip (**maxLines = 2**).
- [USER-SKIP] **Share overlay** instruction **ElevatedCard** wraps body text without truncation of critical lines (scroll if needed).
- [USER-SKIP] **SharingInProgress** body copy remains scrollable if the column exceeds viewport (verify on small phone + largest font).
- [USER-SKIP] Touch targets for **Next**, **Replay**, **Previous** remain at least **48dp** height.

## Contrast (L.15)

- [USER-SKIP] **Share backdrop**: Scrim + **primaryContainer** card meet readable contrast for **title** and **body** in **light** and **dark** theme (including **dynamic color**).
- [USER-SKIP] **SharingInProgress** secondary hint uses **onSurfaceVariant**; primary actions use default **Button** / **Filled** roles.

## Focus order

- [USER-SKIP] Dialogs trap focus appropriately; **Back** dismisses dialogs without losing data unexpectedly.
- [USER-SKIP] **Onboarding** (full-screen): system **Back** on page 2 returns to page 1 (**`BackHandler`**); predictive back enabled on **`MainActivity`** (**`enableOnBackInvokedCallback`**).
- [USER-SKIP] No critical action only reachable through hover or non-keyboard path.

## Automated gate (run locally / before release)

These do **not** replace TalkBack or a large-font eye pass; they catch obvious regressions cheaply.

| Command | Purpose |
| :--- | :--- |
| `./gradlew :app:lintDebug` | Android Lint (content labels, touch targets where lint applies, etc.). |
| `./gradlew :app:connectedDebugAndroidTest` | Cold launch + deeplink smoke (`MainActivity` + Compose); package check. |

**2026-05-07:** `lintDebug`, `test`, and `connectedDebugAndroidTest` all passed in dev environment.

## Verification log (hardware — tick sections above, then fill)

| Field | Value |
| :--- | :--- |
| **Date** | _YYYY-MM-DD (after your device pass)_ |
| **Device** | _e.g. Pixel 8_ |
| **OS** | _e.g. Android 15 (API 35)_ |
| **Font scale** | _e.g. 200% (Display size + Font size in Settings)_ |
| **TalkBack** | _On / Off during pass_ |
| **Notes** | _Failures, OEM quirks, or “all clear”_ |

**Milestone Q.1** in **`docs/BUILD_PLAN.md`** is closed on **automated lint + instrumented smoke**; finish the manual rows here before a major store release if you want full vocal/contrast coverage.
