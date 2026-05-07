# Release smoke checklist (Milestone **P.8**)

Run before tagging a release when changes touch **R8**, **Hilt**, **kotlinx.serialization**, Room migrations, or sharing/backup core paths.

| Step | Action |
| :--- | :--- |
| 1 | Install **signed** **`assembleRelease`** APK (see **`docs/LOCAL_RELEASE_BUILD.md`**) or Play internal track build. |
| 2 | Cold launch → main screen loads; no crash. |
| 3 | **Sequential share**: start from a host app → overlay → pick group → complete at least one handoff (Next / Skip as appropriate). |
| 4 | **Encrypted backup**: export with passphrase → import / verify groups (or spot-check JSON decrypt path). |

Note failures with device/OS and link to the release commit or tag.
