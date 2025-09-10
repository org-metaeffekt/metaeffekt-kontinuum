#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="convert/convert_inventory-to-cyclonedx-01.sh"

# Check if config.sh exists and source it
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
  CMD=(mvn -f "$PROCESSORS_DIR/convert/convert_inventory-to-cyclonedx.xml" process-resources)
  CMD+=("-Dinput.inventory.file=$INPUT_INVENTORY_FILE")
  CMD+=("-Dparam.document.name=$DOCUMENT_NAME")
  CMD+=("-Dparam.document.description=$DOCUMENT_DESCRIPTION")
  CMD+=("-Dparam.document.organization=$DOCUMENT_ORGANIZATION")
  CMD+=("-Dparam.document.organization.url=$DOCUMENT_ORGANIZATION_URL")
  CMD+=("-Doutput.bom.file=$OUTPUT_BOM_FILE")
  CMD+=("-Doutput.format=$OUTPUT_FORMAT")

  echo "${CMD[@]}"
  "${CMD[@]}"
}

main() {
  check_shared_config
  run_maven_command
}

main "$@"