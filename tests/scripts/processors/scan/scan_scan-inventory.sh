#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
LOGGER_PATH="$SCRIPT_DIR/../log.sh"
CASE="scan/scan_scan-inventory-01.sh"


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



#Run maven command
run_maven_command() {
  CMD=(mvn -f "$PROCESSORS_DIR/scan/scan_scan-inventory.xml" process-resources)
  [ -n "${AE_CORE_VERSION:-}" ] && CMD+=("-Dae.core.version=$AE_CORE_VERSION")
  [ -n "${AE_ARTIFACT_ANALYSIS_VERSION:-}" ] && CMD+=("-Dae.artifact.analysis.version=$AE_ARTIFACT_ANALYSIS_VERSION")
  CMD+=("-Dinput.inventory.file=$INPUT_INVENTORY_FILE")
  CMD+=("-Doutput.inventory.file=$OUTPUT_INVENTORY_FILE")
  CMD+=("-Dinput.output.analysis.base.dir=$ANALYSIS_BASE_DIR")
  CMD+=("-Dinput.properties.file=$PROPERTIES_FILE")
  CMD+=("-Denv.kosmos.password=$ENV_KOSMOS_PASSWORD")
  CMD+=("-Denv.kosmos.userkeys.file=$ENV_KOSMOS_USERKEYS_FILE")


  log_info "Running processor $PROCESSORS_DIR/scan/scan_scan-inventory.xml"

  log_config "input.inventory.file=$INPUT_INVENTORY_FILE
              input.output.analysis.base.dir=$ANALYSIS_BASE_DIR
              input.properties.file=$PROPERTIES_FILE" "
              output.inventory.file=$OUTPUT_INVENTORY_FILE"

  log_mvn "${CMD[*]}"

  if [ -f "$OUTPUT_INVENTORY_FILE" ];then
    rm "$OUTPUT_INVENTORY_FILE"
  fi

  if "${CMD[@]}" 2>&1 | while IFS= read -r line; do log_mvn "$line"; done; then
      log_info "Successfully ran $PROCESSORS_DIR/scan/scan_scan-inventory.xml"
  else
      log_error "Failed to run $PROCESSORS_DIR/scan/scan_scan-inventory.xml because the maven execution was unsuccessful"
      return 1
  fi
}

main() {
  local case_file="$CASE"
  local log_level="CONFIG"
  local log_file="$SCRIPT_DIR/../../../../.logs/$(basename $0).log"
  local console_output_enabled=true

  while getopts "c:l:f:ho" flag; do
            case "$flag" in
                c) case_file="$OPTARG" ;;
                h) print_usage; exit 0 ;;
                l) log_level="$OPTARG" ;;
                f) log_file="$OPTARG" ;;
                o) console_output_enabled=false ;;
                *) print_usage; exit 1 ;;
            esac
      done

  initialize_logger "$log_level" "$console_output_enabled" "$log_file"
  check_shared_config
  source_case_file "$case_file"

  run_maven_command
}

main "$@"