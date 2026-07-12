#!/usr/bin/env bash
# Resolve JDK for Android Gradle gates (Linux/macOS/Windows Git Bash).
# Usage: source "$ROOT/scripts/lib/resolve-java-home.sh" && require_java_home
# Or:    bash scripts/lib/resolve-java-home.sh --print

resolve_java_home_dir() {
  local home java_bin

  if [ -n "${JAVA_HOME:-}" ]; then
    home="${JAVA_HOME%/}"
    if [ -x "$home/bin/java" ] || [ -f "$home/bin/java.exe" ]; then
      printf '%s\n' "$home"
      return 0
    fi
  fi

  java_bin="$(command -v java 2>/dev/null || true)"
  if [ -z "$java_bin" ]; then
    java_bin="$(command -v java.exe 2>/dev/null || true)"
  fi
  if [ -n "$java_bin" ] && [ -x "$java_bin" ]; then
    home="$(CDPATH= cd "$(dirname "$java_bin")/.." && pwd)"
    if [ -d "$home" ]; then
      printf '%s\n' "$home"
      return 0
    fi
  fi

  for home in \
    "/mnt/c/Program Files/Microsoft"/jdk-*-hotspot \
    "/c/Program Files/Microsoft"/jdk-*-hotspot \
    "/mnt/c/Program Files/Android/Android Studio/jbr" \
    "/c/Program Files/Android/Android Studio/jbr" \
    "/usr/lib/jvm/java-17-openjdk" \
    "/usr/lib/jvm/java-17-openjdk-amd64"; do
    [ -d "$home" ] || continue
    if [ -x "$home/bin/java" ] || [ -f "$home/bin/java.exe" ]; then
      printf '%s\n' "$home"
      return 0
    fi
  done
  return 1
}

# Spaces in "Program Files" break PATH entries; shim java into a no-space dir.
_install_java_shim() {
  local home="$1"
  local java_exe="$home/bin/java"
  if [ ! -x "$java_exe" ]; then
    java_exe="$home/bin/java.exe"
  fi
  [ -f "$java_exe" ] || return 1
  local shim_dir="${TMPDIR:-/tmp}/mas-java-shim"
  mkdir -p "$shim_dir"
  # shellcheck disable=SC2016
  printf '#!/bin/sh\nexec "%s" "$@"\n' "$java_exe" >"$shim_dir/java"
  chmod +x "$shim_dir/java"
  export PATH="$shim_dir:$PATH"
}

require_java_home() {
  local home
  if ! home="$(resolve_java_home_dir)"; then
    echo "ERROR: JAVA_HOME not found (install JDK 17+ or set JAVA_HOME)" >&2
    return 1
  fi
  export JAVA_HOME="$home"
  if [ -x "$JAVA_HOME/bin/java" ]; then
    export PATH="$JAVA_HOME/bin:$PATH"
  else
    # Windows Git Bash: only java.exe; PATH cannot hold "Program Files" safely
    _install_java_shim "$JAVA_HOME" || return 1
    # Unset so ./gradlew uses `java` from PATH (shim), not missing bin/java
    unset JAVA_HOME
  fi
  if ! command -v java >/dev/null 2>&1; then
    echo "ERROR: java not executable on PATH after resolving JDK at $home" >&2
    return 1
  fi
  return 0
}

if [ "${1:-}" = "--print" ]; then
  resolve_java_home_dir || exit 1
  exit 0
fi
