# Feature: modal-destinations

> History, About, and Modify apps are full-screen destinations. Dialogs stay for short confirms. BUILD_PLAN **UX-018**.

## Acceptance criteria

- History is a `Scaffold` screen (back in the app bar; last re-share is an app-bar action, not a second dialog primary)
- About is a screen with one chrome action (back); Feedback is an in-body link
- Modify apps is a full-screen destination with Save in the app bar
- Settings remains the existing full-screen destination

## Tests

- Automated: Paparazzi `MilestoneAbPaparazziTest.historyDialog_rowReshare_light` snapshots `HistoryScreen`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
