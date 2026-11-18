#!/usr/bin/env bash
# Validate F-Droid/Fastlane metadata scaffold (AGENT gate; APK hashes remain ADB).
# Usage: scripts/verify-fdroid-metadata.sh
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

if [ -d "$ROOT/examples/android/metadata/en-US" ]; then
  META="$ROOT/examples/android/metadata/en-US"
  GRADLE="$ROOT/examples/android/app/build.gradle.kts"
elif [ -d "$ROOT/fastlane/metadata/android/en-US" ]; then
  # Android child (this repo): Fastlane metadata at repo root
  META="$ROOT/fastlane/metadata/android/en-US"
  GRADLE="$ROOT/app/build.gradle.kts"
else
  META="$ROOT/examples/android/metadata/en-US"
  GRADLE="$ROOT/examples/android/app/build.gradle.kts"
fi
ERRORS=0

fail() {
  echo "FAIL: $1"
  ERRORS=$((ERRORS + 1))
}

ok() {
  echo "OK   $1"
}

if [ ! -d "$META" ]; then
  fail "missing $META"
else
  ok "metadata directory present"
fi

for f in title.txt short_description.txt full_description.txt; do
  if [ ! -s "$META/$f" ]; then
    fail "missing or empty $META/$f"
  else
    ok "$f present"
  fi
done

if [ ! -f "$GRADLE" ]; then
  fail "missing $GRADLE"
else
  VERSION_CODE="$(grep -E 'versionCode\s*=' "$GRADLE" | head -1 | sed -E 's/.*=\s*([0-9]+).*/\1/')"
  VERSION_NAME="$(grep -E '^[[:space:]]*versionName[[:space:]]*=[[:space:]]*"[^"]+"' "$GRADLE" | head -1 | sed 's/.*"\(.*\)".*/\1/')"
  APP_VERSION="$(grep -E 'val appVersion\s*=' "$GRADLE" | head -1 | sed 's/.*"\(.*\)".*/\1/')"
  if [ -z "${VERSION_CODE:-}" ]; then
    fail "could not parse versionCode from build.gradle.kts"
  elif [ ! -s "$META/changelogs/${VERSION_CODE}.txt" ]; then
    fail "missing changelog $META/changelogs/${VERSION_CODE}.txt"
  else
    ok "changelog for versionCode ${VERSION_CODE}"
  fi
  if [ -z "${VERSION_NAME:-}" ]; then
    fail "F-Droid checkupdates needs a quoted versionName = \"…\" in $GRADLE"
  else
    ok "quoted versionName ${VERSION_NAME} (F-Droid checkupdates)"
  fi
  if [ -n "${APP_VERSION:-}" ] && [ "$APP_VERSION" != "$VERSION_NAME" ]; then
    fail "val appVersion ($APP_VERSION) must match quoted versionName ($VERSION_NAME)"
  fi
fi

if [ ! -d "$META/images" ]; then
  if [ -d "$ROOT/examples/android" ]; then
    fail "missing $META/images/ (add README + assets before submit)"
  else
    echo "WARN: missing $META/images/ (store screenshots stay [ADB]/human for this child)"
  fi
else
  ok "images directory present"
fi

if [ -f "$ROOT/LICENSE" ]; then
  ok "root LICENSE present (MIT for template)"
else
  fail "missing root LICENSE"
fi

if [ -d "$ROOT/examples/android/fastlane/metadata/android/en-US" ]; then
  ok "fastlane metadata mirror present"
fi

echo ""
echo "SKIP [ADB] reproducible APK hash verification — run on device/emulator per modules/android/MODULE.md"

# F-Droid scan strips signingConfigs; getByName("release") then fails configuration.
if [ -f "$GRADLE" ]; then
  if grep -E 'signingConfigs\.getByName\("release"\)' "$GRADLE" >/dev/null 2>&1; then
    fail "$GRADLE must use signingConfigs.findByName/maybeCreate(\"release\"), not getByName"
  else
    ok "release signingConfig is F-Droid-scan tolerant"
  fi
fi

# Dummy/placeholder screenshots fail even when metadata text is present.
if ! bash "$ROOT/scripts/check-fdroid-screenshots.sh"; then
  fail "dummy F-Droid screenshots are not allowed"
fi

PHONE_SHOTS="$META/images/phoneScreenshots"
if [ ! -d "$PHONE_SHOTS" ] || ! find "$PHONE_SHOTS" -maxdepth 1 -type f \( -name '*.png' -o -name '*.jpg' -o -name '*.jpeg' \) | grep -q .; then
  fail "missing real PNGs in $PHONE_SHOTS (F-Droid listing)"
else
  ok "phoneScreenshots present"
fi

if [ "$ERRORS" -gt 0 ]; then
  echo "${ERRORS} F-Droid metadata check(s) failed"
  exit 1
fi

echo "F-Droid metadata scaffold verified"
