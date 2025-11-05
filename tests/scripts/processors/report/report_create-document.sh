#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PRELOAD_SCRIPT_PATH="$SCRIPT_DIR/../preload.sh"
SHARED_SCRIPT_PATH="$SCRIPT_DIR/../shared.sh"
LOGGER_PATH="$SCRIPT_DIR/../log.sh"
CASE="report/report_create-document-01.sh"


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
  CMD=(mvn -f "$PROCESSORS_DIR/report/report_create-document.xml" verify)
  [ -n "${AE_CORE_VERSION:-}" ] && CMD+=("-Dae.core.version=$AE_CORE_VERSION")
  [ -n "${AE_ARTIFACT_ANALYSIS_VERSION:-}" ] && CMD+=("-Dae.artifact.analysis.version=$AE_ARTIFACT_ANALYSIS_VERSION")
  CMD+=("-Dinput.asset.descriptor.dir=$INPUT_ASSET_DESCRIPTOR_DIR")
  CMD+=("-Dinput.asset.descriptor.path=$INPUT_ASSET_DESCRIPTOR_PATH")
  CMD+=("-Dinput.inventory.file=$INPUT_INVENTORY_FILE")
  CMD+=("-Dinput.reference.inventory.file=$INPUT_REFERENCE_INVENTORY_FILE")
  CMD+=("-Dinput.reference.license.dir=$INPUT_REFERENCE_LICENSE_DIR")
  CMD+=("-Dinput.reference.component.dir=$INPUT_REFERENCE_COMPONENT_DIR")
  CMD+=("-Doutput.document.file=$OUTPUT_DOCUMENT_FILE")
  CMD+=("-Doutput.computed.inventory.path=$OUTPUT_COMPUTED_INVENTORY_DIR")
  CMD+=("-Dparam.security.policy.file=$PARAM_SECURITY_POLICY_FILE")
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