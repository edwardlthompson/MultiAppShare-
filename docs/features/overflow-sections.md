# Feature: overflow-sections

> Logical section dividers in the main overflow menu grouping Share, Data, and About/Settings actions. BUILD_PLAN **AE.62**.

## Acceptance criteria

- ✅ `MainScreenOverflowMenu` organizes items into Share Actions (Sort, Share Clipboard), Data Management (History, Export, Import), and Preferences/About (Language, Theme, Delay, About, Donate)
- ✅ `HorizontalDivider` elements provide clear visual and accessibility section boundaries
- ✅ Preserves all menu callbacks

## Fallback validation

- Command: `bash scripts/feature-gate.sh --stack android`
