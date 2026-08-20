# Feature: group-and-theme-ux

## Acceptance criteria

- Persistable inbound content URI grants are attempted; hosts without the persistable flag still require a re-send.
- Groups can be renamed (name PK rewrite) and merged; delete shows a Snackbar undo.
- Theme is system / light / dark; share delay presets 0 / 500 / 1000 / 2000 ms.
- Skip this app advances one step; notification Next / Skip / Cancel reach MainActivity extras.

## Smoke scenario

1. Share a photo from an app that grants persistable access, force-stop, reopen — overlay returns and the URI still opens.
2. Rename a group; pin shortcut toast appears.
3. Delete a group and tap Undo.
4. Merge Social into Work; source disappears, apps unioned.
5. Menu → Theme → Dark; Menu → Share delay → 1000 ms; skip one app from overlay and from the notification.

## Definition of Done

Unit tests for persistable flags, rename, merge, delay clamp. `feature-gate.sh --stack android` after AGENT work.
