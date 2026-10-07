#!/usr/bin/env bash

# Shared helpers for the small, standalone processor tests.

if [[ "${KONTINUUM_TEST_LIB_LOADED:-}" == "1" ]]; then
  return 0 2>/dev/null || exit 0
fi
KONTINUUM_TEST_LIB_LOADED=1

readonly SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
readonly KONTINUUM_DIR="$(cd "$SELF_DIR/../.." && pwd)"
readonly HARNESS_DIR="$(cd "$KONTINUUM_DIR/.." && pwd)"
readonly TEST_DIR="$(cd "$KONTINUUM_DIR/tests" && pwd)"
readonly TEST_RESOURCES_DIR="$(cd "$TEST_DIR/resources" && pwd)"
readonly PROCESSORS_DIR="$(cd "$KONTINUUM_DIR/processors" && pwd)"
readonly TEST_TARGET_DIR="$TEST_DIR/target"


fail() {
  printf 'ERROR: %s\n' "$*" >&2
  exit 1
}

_load_properties_file() {
  local properties_file="$1"
  local line key value variable

  while IFS= read -r line || [[ -n "$line" ]]; do
    line="${line//$'\r'/}"
    line="${line#"${line%%[![:space:]]*}"}"
    line="${line%"${line##*[![:space:]]}"}"
    [[ -z "$line" || "$line" == \#* || "$line" == \!* ]] && continue

    if [[ "$line" =~ ^([^=:]+)[=:](.*)$ ]]; then
      key="${BASH_REMATCH[1]}"
      value="${BASH_REMATCH[2]}"
      key="${key#"${key%%[![:space:]]*}"}"
      key="${key%"${key##*[![:space:]]}"}"
      value="${value#"${value%%[![:space:]]*}"}"
      value="${value%"${value##*[![:space:]]}"}"

      if [[ "$value" =~ ^\"(.*)\"$ ]] || [[ "$value" =~ ^\'(.*)\'$ ]]; then
        value="${BASH_REMATCH[1]}"
      fi

      variable="$(printf '%s' "$key" | tr '[:lower:]' '[:upper:]' | tr '.-' '__')"
      [[ "$variable" =~ ^[A-Z_][A-Z0-9_]*$ ]] || continue
      printf -v "$variable" '%s' "$value"
      export "$variable"
    fi
  done < "$properties_file"
}

load_local_properties() {
  # Load the harness file as shared defaults, then let submodule-local values win.
  if [[ -f "$HARNESS_DIR/.local.properties" ]]; then
    _load_properties_file "$HARNESS_DIR/.local.properties"
  fi
  if [[ -f "$KONTINUUM_DIR/.local.properties" ]]; then
    _load_properties_file "$KONTINUUM_DIR/.local.properties"
  fi

  if [[ ! -f "$HARNESS_DIR/.local.properties" && ! -f "$KONTINUUM_DIR/.local.properties" ]]; then
    return 0
  fi

  # Keep the historical aliases used by processor parameters.
  if [[ -n "${AE_WORKBENCH_DIR:-}" && -z "${EXTERNAL_WORKBENCH_DIR:-}" ]]; then
    EXTERNAL_WORKBENCH_DIR="$AE_WORKBENCH_DIR"
  elif [[ -n "${EXTERNAL_WORKBENCH_DIR:-}" && -z "${AE_WORKBENCH_DIR:-}" ]]; then
    AE_WORKBENCH_DIR="$EXTERNAL_WORKBENCH_DIR"
  fi
  export AE_WORKBENCH_DIR EXTERNAL_WORKBENCH_DIR

  if [[ -n "${AE_KONTINUUM_DIR:-}" && -z "${EXTERNAL_KONTINUUM_DIR:-}" ]]; then
    EXTERNAL_KONTINUUM_DIR="$AE_KONTINUUM_DIR"
  elif [[ -n "${EXTERNAL_KONTINUUM_DIR:-}" && -z "${AE_KONTINUUM_DIR:-}" ]]; then
    AE_KONTINUUM_DIR="$EXTERNAL_KONTINUUM_DIR"
  fi
  export AE_KONTINUUM_DIR EXTERNAL_KONTINUUM_DIR

  if [[ -n "${VULNERABILITY_MIRROR_DIR:-}" && -z "${EXTERNAL_VULNERABILITY_MIRROR_DIR:-}" ]]; then
    EXTERNAL_VULNERABILITY_MIRROR_DIR="$VULNERABILITY_MIRROR_DIR"
  elif [[ -n "${EXTERNAL_VULNERABILITY_MIRROR_DIR:-}" && -z "${VULNERABILITY_MIRROR_DIR:-}" ]]; then
    VULNERABILITY_MIRROR_DIR="$EXTERNAL_VULNERABILITY_MIRROR_DIR"
  fi
  export VULNERABILITY_MIRROR_DIR EXTERNAL_VULNERABILITY_MIRROR_DIR

  if [[ -n "${VULNERABILITY_MIRROR_URL:-}" && -z "${EXTERNAL_VULNERABILITY_MIRROR_URL:-}" ]]; then
    EXTERNAL_VULNERABILITY_MIRROR_URL="$VULNERABILITY_MIRROR_URL"
  elif [[ -n "${EXTERNAL_VULNERABILITY_MIRROR_URL:-}" && -z "${VULNERABILITY_MIRROR_URL:-}" ]]; then
    VULNERABILITY_MIRROR_URL="$EXTERNAL_VULNERABILITY_MIRROR_URL"
  fi
  export VULNERABILITY_MIRROR_URL EXTERNAL_VULNERABILITY_MIRROR_URL
}

require_config() {
  local variable="$1"
  [[ "$variable" =~ ^[A-Z_][A-Z0-9_]*$ ]] || fail "Invalid configuration variable name: $variable"
  [[ -n "${!variable:-}" ]] || fail "Required .local.properties value is missing: $variable"
}

require_file() {
  [[ -f "$1" ]] || fail "Required test input file not found: $1"
}

require_directory() {
  [[ -d "$1" ]] || fail "Required test input directory not found: $1"
}

new_test_output_dir() {
  local domain="$1"
  local processor="$2"
  local parent="$TEST_TARGET_DIR/$domain/$processor"
  local timestamp

  [[ "$domain" =~ ^[A-Za-z0-9._-]+$ ]] || fail "Invalid processor domain: $domain"
  [[ "$processor" =~ ^[A-Za-z0-9._-]+$ ]] || fail "Invalid processor name: $processor"

  mkdir -p "$parent"
  timestamp="$(date -u '+%Y%m%dT%H%M%SZ')"
  TEST_OUTPUT_DIR="$(mktemp -d "$parent/${timestamp}-XXXXXX")"
  export TEST_OUTPUT_DIR
}

setup_processor_test() {
  local domain="$1"
  local processor="$2"

  load_local_properties
  new_test_output_dir "$domain" "$processor"
  PROCESSOR_POM="$PROCESSORS_DIR/$domain/$processor.xml"
  export PROCESSOR_POM
  printf 'Output directory: %s\n' "$TEST_OUTPUT_DIR"
}

run_processor() {
  local pom="$1"
  shift

  [[ -f "$pom" ]] || fail "Processor POM not found: $pom"
  command -v mvn >/dev/null 2>&1 || fail "Maven (mvn) is required to run processor tests"

  mvn --batch-mode -f "$pom" process-resources "$@"
}

assert_exists() {
  local path="$1"
  [[ -e "$path" ]] || fail "Expected processor output was not created: $path"
}

assert_nonempty_directory() {
  local path="$1"
  [[ -d "$path" ]] || fail "Expected processor output directory was not created: $path"
  find "$path" -type f -print -quit | grep -q . || fail "Expected processor output directory to contain files: $path"
}
