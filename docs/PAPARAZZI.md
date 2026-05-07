# Paparazzi screenshot tests (**K.3**)

## Setup

- **Plugin**: `app.cash.paparazzi` (see **`gradle/libs.versions.toml`**).
- **Jetifier**: must stay **`android.enableJetifier=false`** in **`gradle.properties`** so the Paparazzi / layoutlib classpath does not hit Jetifier transform failures on **`com.android.tools:common`**.

## Tests

- **`EmptyGroupsPlaceholderPaparazziTest`**: **`EmptyGroupsPlaceholder`** inside **`MultiAppShareTheme`**, **light** and **dark** (no dynamic color, so layoutlib stays stable).

## Goldens

- Checked in under **`app/src/test/snapshots/images/`**.
- **Update** after intentional UI changes:

  ```bash
  ./gradlew :app:recordPaparazziDebug
  ```

- **Verify** (also runs as part of **`./gradlew test`**):

  ```bash
  ./gradlew :app:verifyPaparazziDebug
  ```

## CI

- **`./gradlew test`** on **`ubuntu-latest`** runs Paparazzi verification with the debug unit-test task graph.
