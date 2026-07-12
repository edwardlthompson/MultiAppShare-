#!/usr/bin/env bash
# Triage open Dependabot PRs: report CI/mergeability; optionally squash-merge green ones.
# Usage:
#   bash scripts/triage-dependabot-prs.sh           # report only
#   bash scripts/triage-dependabot-prs.sh --apply   # squash-merge green+MERGEABLE Dependabot PRs
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
# shellcheck source=lib/resolve-gh.sh
source "$ROOT/scripts/lib/resolve-gh.sh"
require_gh || exit 1

APPLY=false
for arg in "$@"; do
  case "$arg" in
    --apply) APPLY=true ;;
    -h|--help)
      echo "Usage: $0 [--apply]"
      exit 0
      ;;
  esac
done

REPO="$(gh repo view --json nameWithOwner -q .nameWithOwner)"
echo "=== Dependabot PR triage for ${REPO} ==="

TMP="$(mktemp)"
trap 'rm -f "$TMP"' EXIT
gh pr list --repo "$REPO" --state open --limit 50 \
  --json number,title,author,mergeable,url,statusCheckRollup >"$TMP"

mapfile -t ROWS < <(python3 "$ROOT/scripts/lib/dependabot_pr_rows.py" "$TMP")

if [ "${#ROWS[@]}" -eq 0 ] || [ "${ROWS[0]}" = "NONE" ]; then
  echo "OK   No open Dependabot PRs"
  exit 0
fi

MERGED=0
WOULD=0
BLOCKED=0

for row in "${ROWS[@]}"; do
  IFS='|' read -r num verdict mergeable title url <<<"$row"
  echo ""
  echo "#${num}  ${verdict}  mergeable=${mergeable}"
  echo "     ${title}"
  echo "     ${url}"

  if [ "$verdict" != "GREEN" ] || [ "$mergeable" != "MERGEABLE" ]; then
    echo "     -> skip (not green+MERGEABLE)"
    BLOCKED=$((BLOCKED + 1))
    continue
  fi

  if [ "$APPLY" != true ]; then
    echo "     -> would merge (pass --apply)"
    WOULD=$((WOULD + 1))
    continue
  fi

  echo "     -> squash-merging..."
  if gh pr merge "$num" --repo "$REPO" --squash --delete-branch; then
    echo "     OK merged #${num}"
    MERGED=$((MERGED + 1))
  else
    echo "     FAIL merge #${num} (conflicts) — requesting @dependabot recreate"
    gh pr comment "$num" --repo "$REPO" --body "@dependabot recreate" >/dev/null || true
    BLOCKED=$((BLOCKED + 1))
  fi
done

echo ""
echo "Summary: merged=${MERGED} would_merge=${WOULD} blocked=${BLOCKED} apply=${APPLY}"
if [ "$APPLY" != true ] && [ "$WOULD" -gt 0 ]; then
  echo "Re-run with --apply to squash-merge green Dependabot PRs."
fi
