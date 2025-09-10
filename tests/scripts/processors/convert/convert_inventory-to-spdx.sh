#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
LOGGER_PATH="$SCRIPT_DIR/../log.sh"
CASE="convert/convert_inventory-to-spdx-01.sh"


check_shared_config() {
  if [[ -f "$CONFIG_PATH" ]]; then
      source "$CONFIG_PATH"
  else
      log_error "Config file not found at $CONFIG_PATH"
      exit 1
  fi
}

initialize_logger() {
    local log_level="$1"
    local console_output_enabled="$2"
    local log_file="$3"
    source $LOGGER_PATH
    logger_init "$log_level" "$log_file" "${console_output_enabled}"
}



# Run maven command
run_maven_command() {
  CMD=(mvn -f "$PROCESSORS_DIR/convert/convert_inventory-to-spdx.xml" process-resources)
  CMD+=("-Dinput.inventory.file=$INPUT_INVENTORY_FILE")
  CMD+=("-Dparam.document.id.prefix=$DOCUMENT_ID_PREFIX")
  CMD+=("-Dparam.document.name=$DOCUMENT_NAME")
  CMD+=("-Dparam.document.description=$DOCUMENT_DESCRIPTION")
  CMD+=("-Dparam.document.organization=$DOCUMENT_ORGANIZATION")
  CMD+=("-Dparam.document.organization.url=$DOCUMENT_ORGANIZATION_URL")
  CMD+=("-Doutput.bom.file=$OUTPUT_BOM_FILE")
  CMD+=("-Doutput.format=$OUTPUT_FORMAT")

  log_info "Running processor $PROCESSORS_DIR/convert/convert_inventory-to-spdx.xml"

  log_config "input.inventory.file=$INPUT_INVENTORY_FILE" "output.bom.file=$OUTPUT_BOM_FILE"

  log_cmd "${CMD[*]}"

  if "${CMD[@]}" 2>&1 | while IFS= read -r line; do log_cmd "$line"; done; then
      log_info "Successfully ran $PROCESSORS_DIR/convert/convert_inventory-to-spdx.xml"
  else
      log_error "Failed to run $PROCESSORS_DIR/convert/convert_inventory-to-spdx.xml because the maven execution was unsuccessful"
      return 1
  fi
}

main() {
  local case_file="$CASE"
  local log_level="ALL"
  local log_file="$SCRIPT_DIR/../../../../.logs/$(basename $0).log"
  local console_output_enabled=false

  while getopts "c:l:f:ho" flag; do
            case "$flag" in
                c) case_file="$OPTARG" ;;
                h) print_usage; exit 0 ;;
                l) log_level="$OPTARG" ;;
                f) log_file="$OPTARG" ;;
                o) console_output_enabled=true ;;
                *) print_usage; exit 1 ;;
            esac
      done

  initialize_logger "$log_level" "$console_output_enabled" "$log_file"
  check_shared_config
  source_case_file "$case_file"

  run_maven_command
}

main "$@"