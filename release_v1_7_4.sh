#!/bin/bash
set -e

# --- 1. CONFIGURATION ---
VERSION_NAME="1.7.4"
VERSION_CODE="174"
PACKAGE_NAME="com.edwardlthompson.multiappshare"
APK_NAME="MultiAppShare-v${VERSION_NAME}-release.apk"
TAG="v${VERSION_NAME}"

echo "🚀 Starting automated release for $TAG..."

# --- 2. SOURCE CODE & README UPDATES ---
echo "📝 Updating versioning and README..."
# Update build.gradle.kts (Generic replacement for versionCode and versionName)
sed -i "s/versionCode = [0-9]*/versionCode = ${VERSION_CODE}/" app/build.gradle.kts
sed -i "s/versionName = \".*\"/versionName = \"${VERSION_NAME}\"/" app/build.gradle.kts

# Update README Icon and Version text
sed -i "s/logo.png/icon.png/g" README.md
sed -i "s/v[0-9]\.[0-9]\.[0-9]/v${VERSION_NAME}/g" README.md

# --- 3. REPOSITORY CLEANUP ---
echo "🧹 Cleaning up redundant files..."
rm -rf build*.log status2.txt logo.png fdroiddata_clone
cp app/src/main/res/drawable/ic_launcher.png icon.png

# --- 4. BUILD & SIGN APK ---
echo "🔨 Building signed APK..."
./gradlew clean assembleRelease
mv app/build/outputs/apk/release/app-release.apk "./${APK_NAME}"

# Verify the build
echo "🔍 Verifying APK version..."
VER_CHECK=$(aapt dump badging "${APK_NAME}" | grep versionCode | awk '{print $3}')
echo "Build result: $VER_CHECK"

# --- 5. GITHUB RELEASE & TAGGING ---
echo "📦 Deploying to GitHub..."
git add .
git commit --allow-empty -m "chore: release ${TAG} - sync branding and versioning"
git push origin main

# Delete stale tag if exists and re-tag
git tag -d "${TAG}" || true
git push origin --delete "${TAG}" || true
git tag "${TAG}"
git push origin "${TAG}"

# Delete existing GitHub release if exists and create a new one
gh release delete "${TAG}" --yes || true

# Create the formal GitHub Release and upload the APK
gh release create "${TAG}" "${APK_NAME}" \
    --title "${TAG} - Official FOSS Release" \
    --notes "Automated release for v${VERSION_NAME}. Fixed F-Droid versioning (v${VERSION_CODE}), updated branding assets, and performed repository cleanup."

# --- 6. GITLAB METADATA SYNC ---
echo "🦊 Syncing GitLab F-Droid Metadata..."
# Note: This assumes you have the fdroiddata repo cloned or accessible locally
# Update the .yml file for your app
METADATA_PATH="../fdroiddata/metadata/${PACKAGE_NAME}.yml"
if [ -f "$METADATA_PATH" ]; then
    sed -i "s/CurrentVersion: .*/CurrentVersion: ${VERSION_NAME}/" "$METADATA_PATH"
    sed -i "s/CurrentVersionCode: .*/CurrentVersionCode: ${VERSION_CODE}/" "$METADATA_PATH"
    # Update the latest build block commit
    # (Simple sed for the last occurrence of 'commit:')
    sed -i "$ s/commit: .*/commit: ${TAG}/" "$METADATA_PATH"
    
    cd ../fdroiddata
    git add .
    git commit --allow-empty -m "update ${PACKAGE_NAME} to ${TAG}"
    git push origin master
    echo "✅ GitLab Metadata synced. Pipeline should trigger shortly."
else
    echo "⚠️ Warning: F-Droid metadata file not found at $METADATA_PATH. Skipping GL sync."
fi

echo "🎉 Release $TAG completed successfully!"
