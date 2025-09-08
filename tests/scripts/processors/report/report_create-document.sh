#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="report/report_create-document-01.sh"


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