#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PRELOAD_SCRIPT_PATH="$SCRIPT_DIR/../preload.sh"
SHARED_SCRIPT_PATH="$SCRIPT_DIR/../shared.sh"
LOGGER_PATH="$SCRIPT_DIR/../log.sh"
CASE="extract/extract_inventory-from-pom-01.sh"


source_shared() {
  if [[ -f "$SHARED_SCRIPT_PATH" ]]; then
      source "$SHARED_SCRIPT_PATH"
  else
      log_error "Config file not found at $SHARED_SCRIPT_PATH"
      exit 1
  fi
}

source_preload() {
  if [ -z "${PRELOAD_LOADED:-}" ]; then
    PRELOAD_LOADED="true"
    export PRELOAD_LOADED

    if [[ -f "$PRELOAD_SCRIPT_PATH" ]]; then
        source "$PRELOAD_SCRIPT_PATH"
    else
        log_error "Config file not found at $PRELOAD_SCRIPT_PATH"
        exit 1
    fi
  fi
}

initialize_logger() {
    local log_file="$1"
    source $LOGGER_PATH
    logger_init "$log_file"
}



#Run maven command
run_maven_command() {
  CMD=(mvn -f "$PROCESSORS_DIR/extract/extract_inventory-from-pom.xml" process-resources)
  [ -n "${AE_CORE_VERSION:-}" ] && CMD+=("-Dae.core.version=$AE_CORE_VERSION")
  [ -n "${AE_ARTIFACT_ANALYSIS_VERSION:-}" ] && CMD+=("-Dae.artifact.analysis.version=$AE_ARTIFACT_ANALYSIS_VERSION")
  CMD+=("-Dinput.pom.file=$INPUT_POM_FILE")
  CMD+=("-Doutput.inventory.file=$OUTPUT_INVENTORY_FILE")
  CMD+=("-Dparam.include.scope.provided=$PARAM_INCLUDE_SCOPE_PROVIDED")
  CMD+=("-Dparam.include.scope.system=$PARAM_INCLUDE_SCOPE_SYSTEM")
  CMD+=("-Dparam.include.scope.test=$PARAM_INCLUDE_SCOPE_TEST")
  CMD+=("-Dparam.include.optional=$PARAM_INCLUDE_SCOPE_OPTIONAL")
  CMD+=("-Dparam.include.scope.plugins=$PARAM_INCLUDE_SCOPE_PLUGINS")

  pass_command_info_to_logger "$(basename "$0")"
}

main() {
  local log_file="$SCRIPT_DIR/../../../../.logs/$(basename $0 .sh).log"

  source_shared

  while getopts "c:f:h" flag; do
            case "$flag" in
                c) CASE="$OPTARG" ;;
                h) print_usage; exit 0 ;;
                f) log_file="$OPTARG" ;;
                *) print_usage; exit 1 ;;
            esac
      done

  initialize_logger "$log_file"
  source_preload
  source_case_file "$CASE"
  run_maven_command
}

main "$@"