# Release Notes: MultiAppShare v1.8.0

### 🏁 Standard v1.8.0 Features

- **Performance:** Baseline profile generation harness + merged profile artifacts (improves cold start / first-frame paths).
- **Security:** Encrypted JSON backup export/import (AES-256-GCM + PBKDF2-HMAC-SHA256) with passphrase dialogs + tests.
- **Toolchain:** Hilt + Room migrated from kapt to **KSP**; Compose compiler deprecation cleanup; CI/Dependabot hygiene.
- **UX:** Main screen refactor into smaller Compose files; sharing overlays and dialogs split for maintainability.
- **i18n:** Added French and Spanish string resources + locale configuration.
- **Release workflow:** Documented novice-friendly local signing + helper scripts for generating signed APKs.

---
*Verified and released by Antigravity.*
