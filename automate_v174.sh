#!/bin/bash
set -e

# 1. Configuration
VERSION_NAME="1.7.4"
VERSION_CODE="174"
APK_NAME="MultiAppShare-v${VERSION_NAME}-release.apk"
TAG="v${VERSION_NAME}"

# 2. Source & README Audit
echo "📝 Updating versioning and README..."
# Update build.gradle.kts (Generic replacement for versionCode and versionName)
sed -i "s/versionCode = [0-9]*/versionCode = ${VERSION_CODE}/" app/build.gradle.kts
sed -i "s/versionName = \".*\"/versionName = \"${VERSION_NAME}\"/" app/build.gradle.kts
sed -i "s/logo.png/icon.png/g" README.md

# Sync branding icon
echo "🧹 Syncing branding icon..."
cp app/src/main/res/drawable/ic_launcher.png icon.png

# 3. Clean & Build Signed APK
echo "🔨 Building signed APK..."
./gradlew clean assembleRelease
mv app/build/outputs/apk/release/app-release.apk "./${APK_NAME}"

# 4. GitHub Release Promotion
echo "📦 Deploying to GitHub..."
git add .
git commit --allow-empty -m "chore: release ${TAG} - sync branding and versioning"
git push origin main

# Manage tags
git tag -d "${TAG}" || true
git push origin --delete "${TAG}" || true
git tag "${TAG}" && git push origin "${TAG}"

# Promoting the tag to a formal Release with the APK
echo "🚀 Creating GitHub Release..."
gh release delete "${TAG}" --yes || true
gh release create "${TAG}" "${APK_NAME}" \
    --title "${TAG} - Official FOSS Release" \
    --notes "Fixed F-Droid versioning (v${VERSION_CODE}), updated branding assets, and performed repository cleanup."

# 5. GitLab Metadata Sync
echo "🦊 Syncing GitLab F-Droid Metadata..."
# (Assumes fdroiddata is in a sibling directory)
cd ../fdroiddata
sed -i "s/CurrentVersion: .*/CurrentVersion: ${VERSION_NAME}/" metadata/com.edwardlthompson.multiappshare.yml
sed -i "s/CurrentVersionCode: .*/CurrentVersionCode: ${VERSION_CODE}/" metadata/com.edwardlthompson.multiappshare.yml
# Update build block commit (assuming it's the last occurrence)
sed -i "$ s/commit: .*/commit: ${TAG}/" metadata/com.edwardlthompson.multiappshare.yml

git add .
git commit --allow-empty -m "update MultiAppShare to ${TAG}"
git push origin master

echo "🎉 Automated release for $TAG completed successfully!"
