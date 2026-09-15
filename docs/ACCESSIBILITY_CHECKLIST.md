# Accessibility checklist (share and groups)

Use this for **K.4** / **L.13** regression passes and **Milestone Q.1** in **`docs/BUILD_PLAN.md`**. Re-run after major UI changes to `MainScreen`, `ui/groups/`, `ui/sharing/`, or dialogs.

Agent-fixed in **UX-029** (still needs a device TalkBack pass): onboarding dots announce “Page N of 2”; group expand/collapse is state-aware; onboarding pager skips animation when animator scale is 0. Keep every **[USER-SKIP]** row as a **[HUMAN]** device check.

## TalkBack / screen reader

- [USER-SKIP] **Main screen**: Top app bar **Groups** is a heading; menu opens; each **DropdownMenuItem** is focusable and announces label.
- [USER-SKIP] **Share overlay**: Top bar title is the only heading; **Choose a group** card is read before the list; each group card is focusable in share mode.
- [USER-SKIP] **Group rows**: Expand/collapse control announces **Expand {name}** / **Collapse {name}**; **More** menu lists modify / reorder / shortcut / delete.
- [USER-SKIP] **Sequential overlay**: **Next App** / **Finish**, **Replay**, **Previous** labels are distinct; step and **Next: {app}** are readable in order.
- [USER-SKIP] **Search groups** (when any groups exist): field has **content description**; empty filter state announces **no_groups_match_filter**.
- [USER-SKIP] **History row Re-share**: 48dp; announces **history_reshare_row**; hidden when the row has no payload.
- [USER-SKIP] **Share clipboard** overflow item announces **menu_share_clipboard**.
- [USER-SKIP] **Retry** after a failed handoff is distinct from **Replay this app**; error line **Couldn't open {app}** is read.

## Display size and font scale (~200%)

- [USER-SKIP] **Groups** title in **TopAppBar** does not clip (**maxLines = 2**).
- [USER-SKIP] **Share overlay** instruction **ElevatedCard** wraps body text without truncation of critical lines (scroll if needed).
- [USER-SKIP] **SharingInProgress** body copy remains scrollable if the column exceeds viewport (verify on small phone + largest font).
- [USER-SKIP] Touch targets for **Next**, **Replay** / **Retry**, **Previous**, History **Re-share**, and overflow items remain at least **48dp** height.
- [USER-SKIP] **History** dialog and **SharingInProgress** remain scrollable at ~200% font.

## Contrast (L.15)

- [USER-SKIP] **Share backdrop**: Scrim is Material 32–60% (`ShareBackdropScrim`); instruction and step copy sit on opaque cards with **onPrimaryContainer** / **onSurface**. Verify light, dark, dynamic color, and low-RAM.
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
