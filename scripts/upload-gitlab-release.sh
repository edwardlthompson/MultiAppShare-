#!/usr/bin/env bash
# Upload release APK asset link to GitLab (optional; requires GITLAB_* env vars).
set -euo pipefail

API_BASE="${GITLAB_API_BASE:-https://gitlab.com/api/v4}"
PROJECT_ID="${GITLAB_PROJECT_ID:?GITLAB_PROJECT_ID required}"
TAG="${RELEASE_TAG:?RELEASE_TAG required}"
APK_PATH="${1:?APK path required}"

APK_NAME="$(basename "$APK_PATH")"
echo "Uploading $APK_NAME to GitLab project $PROJECT_ID (tag $TAG)"

UPLOAD_JSON="$(curl -fsS \
  --header "PRIVATE-TOKEN: ${GITLAB_TOKEN}" \
  --form "file=@${APK_PATH}" \
  "${API_BASE}/projects/${PROJECT_ID}/uploads")"

REL_URL="$(python3 -c 'import json,sys; print(json.loads(sys.stdin.read()).get("url",""))' <<<"$UPLOAD_JSON")"

if [[ -z "$REL_URL" ]]; then
  echo "GitLab upload response did not include a url. Response was:"
  echo "$UPLOAD_JSON"
  exit 1
fi

PROJECT_WEB_URL="$(curl -fsS \
  --header "PRIVATE-TOKEN: ${GITLAB_TOKEN}" \
  "${API_BASE}/projects/${PROJECT_ID}" \
  | python3 -c 'import json,sys; print(json.loads(sys.stdin.read()).get("web_url",""))')"

FILE_URL="${PROJECT_WEB_URL}${REL_URL}"

CREATE_PAYLOAD="$(python3 -c "import json; print(json.dumps({'name': '${TAG}', 'tag_name': '${TAG}', 'description': 'Automated release asset upload from GitHub Actions.', 'assets': {'links': [{'name': '${APK_NAME}', 'url': '${FILE_URL}'}]}}))")"

if curl -fsS --request POST \
  --header "PRIVATE-TOKEN: ${GITLAB_TOKEN}" \
  --header "Content-Type: application/json" \
  --data "$CREATE_PAYLOAD" \
  "${API_BASE}/projects/${PROJECT_ID}/releases" >/dev/null; then
  echo "Created GitLab release $TAG with asset link."
  exit 0
fi

LINK_PAYLOAD="$(python3 -c "import json; print(json.dumps({'name': '${APK_NAME}', 'url': '${FILE_URL}'}))")"

curl -fsS --request POST \
  --header "PRIVATE-TOKEN: ${GITLAB_TOKEN}" \
  --header "Content-Type: application/json" \
  --data "$LINK_PAYLOAD" \
  "${API_BASE}/projects/${PROJECT_ID}/releases/${TAG}/assets/links" >/dev/null

echo "Added/updated GitLab release asset link for $TAG."
