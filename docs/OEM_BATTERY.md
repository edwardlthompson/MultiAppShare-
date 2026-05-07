# OEM and battery optimization checklist

Some devices aggressively **freeze or kill** foreground services or delay notifications when **battery saver** or vendor “optimization” is enabled. Multi App Share uses a **foreground service** during sequential sharing and a **low-importance** notification so target apps’ compose fields stay usable.

## Manual smoke matrix (pre-release or when users report “sharing stopped”)

| Check | Action |
|--------|--------|
| Battery saver | With saver **on**, run a 3-app sequential share; confirm notification still appears and **Next App** flow completes. |
| Vendor optimization | On Xiaomi / Oppo / Samsung etc., check **Auto-start** / **Battery → Unrestricted** for this app if FGS is killed mid-flow. |
| Notification policy | Confirm the user has not **blocked** the “Sharing progress” channel. |
| Aggressive task kill | After starting share, force-stop from Settings → confirm expected **session loss** (see **`docs/PROCESS_DEATH.md`**). |

## Optional user-facing path (FOSS-safe)

If FGS is killed despite policy, an optional deep link to **ignore battery optimizations** can be offered **only** from in-app copy (user-initiated), not silent. Keep wording neutral: “If sharing stops in the background on your device, you can allow unrestricted battery use for Multi App Share.” No third-party analytics.

## Reporting template

- Device model / Android version  
- OEM “battery” or “autostart” settings  
- Whether notification channel was blocked  
- Steps: MIME type, number of apps in group, where flow stopped  

File an issue with this block for reproducibility.
