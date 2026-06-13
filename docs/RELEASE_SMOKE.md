# Release smoke checklist (Milestone **P.8** / **S.9.2**)

Run before tagging a release when changes touch **R8**, **Hilt**, **kotlinx.serialization**, Room migrations, or sharing/backup core paths.

| Step | Action |
| :--- | :--- |
| 1 | Install **signed** **`assembleRelease`** APK (see **`docs/LOCAL_RELEASE_BUILD.md`**) or Play internal track build. |
| 2 | Cold launch → main screen loads; no crash. |
| 3 | **Sequential share**: start from a host app → overlay → pick group → complete at least one handoff (Next / Skip as appropriate). |
| 4 | **Share rotation** (S.4.1): rotate device mid-share overlay → session state preserved. |
| 5 | **Failed share target** (S.7.1): share to a group with a disabled/incompatible app → skip or error UI; no hang. |
| 6 | **Encrypted backup**: export with passphrase → import / verify groups (or spot-check JSON decrypt path). |
| 7 | **Deeplink expand** (S.6.1): cold launch `multiappshare://group?name=…` for an existing group → group visible/expanded. |

Note failures with device/OS and link to the release commit or tag.
