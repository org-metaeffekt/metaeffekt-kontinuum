#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SHARED_SCRIPT_PATH="$SCRIPT_DIR/../shared.sh"
LOGGER_PATH="$SCRIPT_DIR/../log.sh"
CASE="convert/convert_inventory-to-spdx-01.sh"


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
  CMD=(mvn -f "$PROCESSORS_DIR/convert/convert_inventory-to-spdx.xml" process-resources)
  [ -n "${AE_CORE_VERSION:-}" ] && CMD+=("-Dae.core.version=$AE_CORE_VERSION")
  [ -n "${AE_ARTIFACT_ANALYSIS_VERSION:-}" ] && CMD+=("-Dae.artifact.analysis.version=$AE_ARTIFACT_ANALYSIS_VERSION")
  CMD+=("-Dinput.inventory.file=$INPUT_INVENTORY_FILE")
  CMD+=("-Dparam.document.id.prefix=$DOCUMENT_ID_PREFIX")
  CMD+=("-Dparam.document.name=$DOCUMENT_NAME")
  CMD+=("-Dparam.document.description=$DOCUMENT_DESCRIPTION")
  CMD+=("-Dparam.document.organization=$DOCUMENT_ORGANIZATION")
  CMD+=("-Dparam.document.organization.url=$DOCUMENT_ORGANIZATION_URL")
  CMD+=("-Doutput.bom.file=$OUTPUT_BOM_FILE")
  CMD+=("-Doutput.format=$OUTPUT_FORMAT")

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