#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SHARED_SCRIPT_PATH="$SCRIPT_DIR/../shared.sh"
LOGGER_PATH="$SCRIPT_DIR/../log.sh"
CASE="util/util_transform-inventories-01.sh"


check_shared_config() {
  if [ -z "${SHARED_CONFIG_LOADED:-}" ]; then
    SHARED_CONFIG_LOADED="true"
    export SHARED_CONFIG_LOADED

    if [[ -f "$SHARED_SCRIPT_PATH" ]]; then
        source "$SHARED_SCRIPT_PATH"
    else
        log_error "Config file not found at $SHARED_SCRIPT_PATH"
        exit 1
    fi
  fi
}

initialize_logger() {
    local log_file="$1"
    source $LOGGER_PATH
    logger_init "$log_file"
}



# Run maven command
run_maven_command() {
  CMD=(mvn -f "$PROCESSORS_DIR/util/util_transform-inventories.xml" process-resources)
  [ -n "${AE_CORE_VERSION:-}" ] && CMD+=("-Dae.core.version=$AE_CORE_VERSION")
  [ -n "${AE_ARTIFACT_ANALYSIS_VERSION:-}" ] && CMD+=("-Dae.artifact.analysis.version=$AE_ARTIFACT_ANALYSIS_VERSION")
  CMD+=("-Dinput.inventory.dir=$INPUT_INVENTORY_DIR")
  CMD+=("-Doutput.inventory.dir=$OUTPUT_INVENTORY_DIR")
  CMD+=("-Dparam.kotlin.script.file=$KOTLIN_SCRIPT_FILE")

  pass_command_info_to_logger "$(basename "$0")"
}

main() {
  local log_file="$SCRIPT_DIR/../../../../.logs/$(basename $0).log"

  while getopts "c:f:h" flag; do
            case "$flag" in
                c) CASE="$OPTARG" ;;
                h) print_usage; exit 0 ;;
                f) log_file="$OPTARG" ;;
                *) print_usage; exit 1 ;;
            esac
      done

  initialize_logger "$log_file"
  check_shared_config
  source_case_file "$CASE"

  run_maven_command
}

main "$@"