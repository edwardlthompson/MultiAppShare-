# Process death and in-flight sharing

## Intended behavior

Multi App Share treats an active **sequential share** as a **foreground session**: the user starts from a share intent, picks a group, and steps through targets with **Next App** / **Finish**.

If Android **kills the process** while sharing (low memory, aggressive OEM, user swipes away from Recents in some configurations):

- **In-memory state** (current step index, selected group, intent extras) is **not** restored automatically after a cold start.
- The user should **re-send from the originating app** (or re-open the share sheet) and choose the group again, or open the app from the launcher and use **History** / shortcuts where applicable.

This matches a **small FOSS utility** with no accounts and no server: persisting partial share state would require careful security and UX around stale sessions (wrong attachment, wrong step).

## Optional hardening (future)

If product requirements change, a minimal approach is a **DataStore snapshot** cleared on **Finish** or **cancel**: group id, step index, and a **nonce** tied to the share `content://` URIs or a hash of the clip—never log URI contents. **Wrong-passphrase-style** validation on resume avoids applying a stale sequence to new share intents.

## How to verify manually

1. Enable **Developer options → Don’t keep activities** (stress test; not identical to low-memory kill but exercises recreation paths).
2. Start a multi-step share; background the app mid-sequence and return—confirm behavior matches expectations on your device.
3. For closer simulation, use **Developer options → Background process limit** or memory pressure tools if available.

Document device results in release notes or issues when behavior surprises users on a specific OEM.
