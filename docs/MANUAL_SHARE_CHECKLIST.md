# Manual checklist — sequential multi-app share

Use this when validating builds where automation is flaky (OEM share sheets, timing, battery savers). Record **device**, **Android version**, and **build** (version name/code).

## Prerequisites

- Debug or release APK installed; at least one group with **two or more** apps that accept the test MIME type (e.g. image/jpeg, text/plain).
- For notification tests: **POST_NOTIFICATIONS** granted if prompted.

## Core flow (share from another app)

1. Open **Photos** (or any app) and share **one image** to MultiAppShare (share sheet → MultiAppShare).
2. Confirm overlay opens with **“Share with a group”** and compatible groups listed.
3. Tap a group — confirm **foreground sharing** / step UI advances (step *k* of *n*).
4. Complete or skip the target app’s composer; return to MultiAppShare; tap **Next App** until **Finish** / completion toast.
5. Confirm **history** (menu → History) shows an entry with plausible status text.

## Multi-item / MIME

6. Share **two or more images** via **ACTION_SEND_MULTIPLE** when possible — confirm group filtering still makes sense and sequence runs.

## Deeplinks (optional)

7. `adb shell am start -a android.intent.action.VIEW -d "multiappshare://open"` — app opens.
8. `adb shell am start -a android.intent.action.VIEW -d "multiappshare://group?name=YourGroupName"` — matching group expands if present.

## Backup / restore (manual)

9. Export encrypted backup from settings; re-import on same device or another install — groups match expectations.

## Notes column

| Step | Pass/Fail | Notes |
| :---: | :---: | :--- |
| 1–5 | | |
| 6 | | |
| 7–8 | | |
| 9 | | |
