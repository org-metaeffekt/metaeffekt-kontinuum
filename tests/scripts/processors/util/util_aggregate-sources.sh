#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="util/util_aggregate-sources-01.sh"


check_shared_config() {
  if [[ -f "$CONFIG_PATH" ]]; then
      source "$CONFIG_PATH"
  else
      echo "Error: config.sh not found at $CONFIG_PATH" >&2
      exit 1
  fi
}

#Run maven command
run_maven_command() {
  CMD=(mvn -f "$PROCESSORS_DIR/util/util_aggregate-sources.xml" process-resources)
  CMD+=("-Dinput.inventory.file=$INPUT_INVENTORY_FILE")
  CMD+=("-Dtarget.dir=$CURRENT_TARGET_DIR")
  CMD+=("-Dannex.source.dir=$ANNEX_SOURCE_DIR")
  CMD+=("-Dretained.sources.dir=$RETAINED_SOURCES_DIR")
  CMD+=("-Dalternative.artifact.source.mapping=$ALTERNATIVE_ARTIFACT_SOURCE_MAPPING")
  CMD+=("-Dinclude.all.sources=$INCLUDE_ALL_SOURCES")
  CMD+=("-Dfail.on.missing.sources=$FAIL_ON_MISSING_SOURCES")

  echo "${CMD[@]}"
  "${CMD[@]}"
}

main() {
  check_shared_config

  local case_file="$CASE"

  while getopts "c:h" flag; do
            case "$flag" in
                c) case_file="$OPTARG" ;;
                h) print_usage; exit 0 ;;
                *) print_usage; exit 1 ;;
            esac
      done

  if [[ -f "$CASES_DIR/$case_file" ]]; then
      source "$CASES_DIR/$case_file"
  elif [[ -f "$case_file" ]]; then
      source "$case_file"
  else
      error_exit "Case [$case_file] does not exist. Must be either relative to [$CASES_DIR] or an absolute path."
  fi

  run_maven_command
}

main "$@"