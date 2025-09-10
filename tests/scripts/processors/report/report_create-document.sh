#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
LOGGER_PATH="$SCRIPT_DIR/../log.sh"
CASE="report/report_create-document-01.sh"


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
  CMD=(mvn -f "$PROCESSORS_DIR/report/report_create-document.xml" verify)
  CMD+=("-Dinput.asset.descriptor.dir=$INPUT_ASSET_DESCRIPTOR_DIR")
  CMD+=("-Dinput.asset.descriptor.path=$INPUT_ASSET_DESCRIPTOR_PATH")
  CMD+=("-Dinput.inventory.file=$INPUT_INVENTORY_FILE")
  CMD+=("-Dinput.reference.inventory.file=$INPUT_REFERENCE_INVENTORY_FILE")
  CMD+=("-Dinput.reference.license.dir=$INPUT_REFERENCE_LICENSE_DIR")
  CMD+=("-Dinput.reference.component.dir=$INPUT_REFERENCE_COMPONENT_DIR")
  CMD+=("-Dinput.security.policy.dir=$INPUT_SECURITY_POLICY_DIR")
  CMD+=("-Doutput.document.file=$OUTPUT_DOCUMENT_FILE")
  CMD+=("-Doutput.computed.inventory.path=$OUTPUT_COMPUTED_INVENTORY_DIR")
  CMD+=("-Dparam.template.dir=$PARAM_TEMPLATE_DIR")
  CMD+=("-Dparam.document.type=$PARAM_DOCUMENT_TYPE")
  CMD+=("-Dparam.document.language=$PARAM_DOCUMENT_LANGUAGE")
  CMD+=("-Dparam.asset.id=$PARAM_ASSET_ID")
  CMD+=("-Dparam.asset.name=$PARAM_ASSET_NAME")
  CMD+=("-Dparam.asset.version=$PARAM_ASSET_VERSION")
  CMD+=("-Dparam.product.name=$PARAM_PRODUCT_NAME")
  CMD+=("-Dparam.product.version=$PARAM_PRODUCT_VERSION")
  CMD+=("-Dparam.product.watermark=$PARAM_PRODUCT_WATERMARK")
  CMD+=("-Dparam.overview.advisors=$PARAM_OVERVIEW_ADVISORS")
  CMD+=("-Dparam.property.selector.organization=$PARAM_PROPERTY_SELECTOR_ORGANIZATION")
  CMD+=("-Denv.kontinuum.processors.dir=$ENV_KONTINUUM_PROCESSORS_DIR")
  CMD+=("-Denv.workbench.processors.dir=$ENV_WORKBENCH_PROCESSORS_DIR")
  CMD+=("-Denv.vulnerability.mirror.dir=$ENV_VULNERABILITY_MIRROR_DIR")

  log_info "Running processor $PROCESSORS_DIR/report/report_create-document.xml"

  log_config "input.asset.descriptor.dir=$INPUT_ASSET_DESCRIPTOR_DIR
              input.asset.descriptor.path=$INPUT_ASSET_DESCRIPTOR_PATH
              input.inventory.file=$INPUT_INVENTORY_FILE
              input.reference.inventory.file=$INPUT_REFERENCE_INVENTORY_FILE
              input.reference.license.dir=$INPUT_REFERENCE_LICENSE_DIR
              input.reference.component.dir=$INPUT_REFERENCE_COMPONENT_DIR
              input.security.policy.dir=$INPUT_SECURITY_POLICY_DIR" "
              output.document.file=$OUTPUT_DOCUMENT_FILE
              output.computed.inventory.path=$OUTPUT_COMPUTED_INVENTORY_DIR"

  log_cmd "${CMD[*]}"

  if "${CMD[@]}" 2>&1 | while IFS= read -r line; do log_cmd "$line"; done; then
      log_info "Successfully ran $PROCESSORS_DIR/report/report_create-document.xml"
  else
      log_error "Failed to run $PROCESSORS_DIR/report/report_create-document.xml because the maven execution was unsuccessful"
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