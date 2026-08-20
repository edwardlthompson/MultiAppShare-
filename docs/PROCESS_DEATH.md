# Process death and in-flight sharing

## Intended behavior

Multi App Share treats an active **sequential share** as a **foreground session**: the user starts from a share intent, picks a group, and steps through targets with **Next App** / **Skip remaining** / **Finish**.

If Android **kills the process** while sharing (low memory, aggressive OEM):

- A DataStore snapshot stores group step, MIME, text, and URI strings (never logged).
- On the next cold start **without** a new `ACTION_SEND`, the snapshot is restored if it is **under two hours** old and still has a payload.
- A **new** share intent replaces the snapshot (payload nonce mismatch).
- **Finish**, **Skip remaining**, or back-cancel clears the in-flight snapshot. A successful or early finish also stores a **last payload** for History → **Re-share last**.

On inbound `ACTION_SEND`, Multi App Share calls `takePersistableUriPermission` for `content:` URIs when the host also set `FLAG_GRANT_PERSISTABLE_URI_PERMISSION`. Grants are released when the session is cleared.

Hosts that omit the persistable flag still only grant a transient read. After process death those URIs may fail; the user re-sends from the originating app.

## How to verify manually

1. Enable **Developer options → Don’t keep activities**.
2. Start a multi-step share; swipe the app from Recents; reopen Multi App Share from the launcher — the step overlay should return.
3. Send a *different* payload from another app — the old snapshot must not resume.
