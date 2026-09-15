# Feature: whats-new-before-donate

> After an update, local changelog shows first; donate is a text action. BUILD_PLAN **UX-063**.

## Acceptance criteria

- ✅ `FirstRunDialogQueue.allowLaunchPrompt` is false while what’s-new is visible
- ✅ `WhatsNewHost` reports visibility so donate/update wait
- ✅ What’s-new dialog includes a Donate text action (`about_donate`)

## Tests

- Automated: `FirstRunDialogQueueTest`

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
