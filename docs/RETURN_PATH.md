# Return path during sequential sharing (Milestone **L.3**)

While a share sequence is running, Multi App Share is usually **behind** the target app. Users can return to continue the flow in these FOSS-safe ways:

1. **Recents** — switch back to Multi App Share from the system overview.
2. **Foreground notification** — a **low-importance** ongoing notification shows progress; expanding it reminds you to open the app to tap **Next App** (no heads-up banner by design).
3. **Launcher** — open **Multi App Share** from the app drawer or home screen; the in-progress overlay remains if the process was not killed.
4. **Deeplink** — `multiappshare://open` (see instrumented tests and README) returns to the main entry.

We do **not** use **`SYSTEM_ALERT_WINDOW`** (draw-over-other-apps) for this flow.
