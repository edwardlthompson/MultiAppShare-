# Release Notes — v1.10.0

## Highlights

- Full Milestone AE product backlog delivery: enhanced accessibility (TalkBack live region, reduced motion burst), granular sharing controls (Try later deferral, per-group delays, pause/resume), quick settings tile, dynamic launcher shortcuts, and home screen widget state model.
- Robust data management: 30/90-day history retention, encrypted backup v2 payload opt-in, passphrase strength meter, group template export/import, and uninstalled package cleanup.
- Expanded localization and device form factor support: German and Italian translations, RTL audit helper, foldable two-pane and desktop windowing adaptations.

## Features

- Try later step deferral on failed share step
- Share progress TalkBack live region announcements
- Reduced motion burst animation override
- Haptic feedback toggle on share step finish
- Per-group delay override and MIME mismatch explanation sheet
- Notification tap resume via share-step deeplink
- Quick Settings tile for clipboard share
- Prune uninstalled packages and live package change receiver
- Group editor search, duplicate app warning, emoji labels, and group notes
- Pinned favorite groups sort above standard usage sort
- Single group template export and merge/replace import strategy
- Autogroup dry-run preview calculation and hidden apps filter
- Order apps by last successful share and collapse unused overlay groups
- 30/90-day history retention pruner and clear history confirmation
- Filter history rows by group and sanitized local JSON history export
- History original URI open with persisted grant verification
- Backup payload opt-in, passphrase strength meter, and backup crash pref opt-in
- Structured Settings catalog and in-app search
- In-app third-party FOSS licenses, privacy policy, and offline changelog
- Overflow menu feedback link and battery optimization explainer
- Android 15 foreground service timeout survival
- System notification channel deep link builder and dynamic launcher shortcut rankings
- Home screen widget state model, foldable two-pane split, and desktop windowing bounds
- German and Italian locale support and RTL direction audit helper
- Full-row merged TalkBack semantics for ThemeDialog toggles
- About installation source detection and FOSS funding links (Liberapay, GitHub Sponsors)

## Security & dependencies

- Updated gitleaks hook in pre-commit
- Zero high/critical vulnerabilities across dependencies
- FOSS compliance verified with strict MIT license checks

---

**Full changelog:** see `CHANGELOG.md`
