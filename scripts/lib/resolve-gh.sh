#!/usr/bin/env bash
# Resolve GitHub CLI binary for Linux/macOS/Windows Git Bash.
# Usage (source):  source "$ROOT/scripts/lib/resolve-gh.sh" && require_gh
#                  then call:  gh ...   (function wrapper)
# Or:  GH_BIN="$(bash scripts/lib/resolve-gh.sh --print)"

resolve_gh_bin() {
  local bin
  # Prefer an executable path. On some Windows Git Bash setups,
  # `command -v gh` exits 0 with empty output — ignore that.
  bin="$(command -v gh 2>/dev/null || true)"
  if [ -n "$bin" ] && [ -x "$bin" ]; then
    printf '%s\n' "$bin"
    return 0
  fi
  bin="$(command -v gh.exe 2>/dev/null || true)"
  if [ -n "$bin" ] && [ -x "$bin" ]; then
    printf '%s\n' "$bin"
    return 0
  fi
  local candidate
  for candidate in \
    "/c/Program Files/GitHub CLI/gh.exe" \
    "/mnt/c/Program Files/GitHub CLI/gh.exe" \
    "/Program Files/GitHub CLI/gh.exe"; do
    if [ -x "$candidate" ]; then
      printf '%s\n' "$candidate"
      return 0
    fi
  done
  return 1
}

require_gh() {
  local bin
  if ! bin="$(resolve_gh_bin)"; then
    echo "ERROR: gh CLI required (https://cli.github.com/)" >&2
    return 1
  fi
  # Must use a global — local bin is out of scope when gh() runs later.
  GH_BIN="$bin"
  export GH_BIN
  # shellcheck disable=SC2329
  gh() { "$GH_BIN" "$@"; }
  if [ -n "${BASH_VERSION:-}" ]; then
    export -f gh >/dev/null 2>&1 || true
  fi
  return 0
}

if [ "${1:-}" = "--print" ]; then
  resolve_gh_bin || exit 1
  exit 0
fi
