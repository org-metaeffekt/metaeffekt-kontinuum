#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="advise/advise_enrich-with-reference-01.sh"

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
  CMD=(mvn -f "$PROCESSORS_DIR/advise/advise_enrich-with-reference.xml" process-resources)
  CMD+=("-Dinput.inventory.file=$INPUT_INVENTORY_FILE")
  CMD+=("-Dinput.reference.inventory.dir=$INPUT_REFERENCE_INVENTORY_DIR")
  CMD+=("-Doutput.inventory.dir=$OUTPUT_INVENTORY_DIR")
  CMD+=("-Doutput.inventory.path=$OUTPUT_INVENTORY_PATH")

  echo "${CMD[@]}"
  "${CMD[@]}"
}

main() {
  check_shared_config
  run_maven_command
}

main "$@"