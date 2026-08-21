# Feature: Milestone AB

> Seven product slices after v1.9.3. Room v2 Sequential lock first. Checklist: 🔲 open · ✅ done · ❌ blocked.

## Acceptance criteria

### AB.1 Room v2

- ✅ `AppGroup.id` unique UUID; `name` stays primary key
- ✅ `HistoryItem.payloadJson` nullable
- ✅ Explicit `Migration(1, 2)` on release; no destructive fallback in release
- ✅ Create / duplicate / import-without-id assign `id`; merge keeps target `id`; rename copies `id`

### AB.2 History row re-share

- ✅ Starting a group share writes payload onto the started-sharing history row
- ✅ Per-row Re-share when payload present; hidden on null migrated rows
- ✅ Restore is overlay only (`sharingStarted = false`); footer Re-share last remains

### AB.3 Shortcuts / deeplink by id

- ✅ New pins use `group.id` and `multiappshare://group?id=&name=`
- ✅ Load heals legacy name-id pins; rename updates id + previous name
- ✅ Re-pin toast only when pin API unsupported

### AB.4 Backup wrapper v2

- ✅ Encrypted envelope format unchanged
- ✅ Wrapper v2 includes groups (with id), optional settings, optional lastPayload
- ✅ Import v1 / raw array generates ids and does not wipe settings

### AB.5 Always-on filter

- ✅ Filter field shows when groups are non-empty and not in share mode

### AB.6 Retry after failed handoff

- ✅ `ACTION_SHARE_FAILED` does not auto-advance; Retry/Replay stays on index
- ✅ Skip this app still advances

### AB.7 Share clipboard

- ✅ Overflow item shares clipboard text or a single `content:` URI
- ✅ Empty clip / `SecurityException` toasts; no new permissions

### AB.8 TalkBack / large font (automated)

- ✅ Semantics and 48dp on History row, filter, Retry, clipboard
- ✅ Paparazzi for History row + filter field
- 🔲 Hardware TalkBack stays `[HUMAN]` (`[USER-SKIP]`)

## Smoke scenario

1. _Given_ groups exist after Room v2 migrate
2. _When_ user shares, fails a handoff, re-shares a history row, filters, and shares clipboard
3. _Then_ ids survive rename, backup restores settings, overlay stays on fail

## Container map

| Layer | Path |
|-------|------|
| Logic | `core-database` migrations, `BackupCodec`, `ShortcutHelper`, `ClipboardShare` |
| View | `DashboardHistoryRow`, `MainScreenGroups`, `SharingInProgress`, overflow |
| Tests | DAO/migration, BackupCodec, GroupMutations id, Paparazzi |
| Wiring | `MainViewModel` / `MainActivitySharing` ≤10 lines per slice |

## Definition of Done

See `docs/FEATURE_MODULES.md`. After each AGENT row: `python3 scripts/agent-run.py watch-agent-gates --once --autofix`.
