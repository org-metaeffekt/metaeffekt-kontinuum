#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="util/util_copy-inventories-01.sh"

# Check if config.sh exists and source it
check_shared_config() {
  if [[ -f "$CONFIG_PATH" ]]; then
      source "$CONFIG_PATH"
  else
      echo "Error: config.sh not found at $CONFIG_PATH" >&2
      exit 1
  fi
}

# Run maven command
run_maven_command() {
  CMD=(mvn -f "$PROCESSORS_DIR/util/util_copy-inventories.xml" process-resources)
  CMD+=("-Dinput.base.dir=$INPUT_BASE_DIR")
  CMD+=("-Dinput.inventories.list=$INPUT_INVENTORIES_LIST")
  CMD+=("-Doutput.inventories.dir=$OUTPUT_INVENTORIES_DIR")

  echo "${CMD[@]}"
  "${CMD[@]}"
}

main() {
  check_shared_config
  run_maven_command
}

main "$@"