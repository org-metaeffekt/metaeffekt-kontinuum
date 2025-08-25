#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="util/util_copy-inventories-01.sh"

# Check if config.sh exists and source it
if [[ -f "$CONFIG_PATH" ]]; then
    source "$CONFIG_PATH"
else
    echo "Error: config.sh not found at $CONFIG_PATH" >&2
    exit 1
fi

# Run maven command
CMD=(mvn -f "$PROCESSORS_DIR/util/util_copy-inventories.xml" process-resources)
CMD+=("-Dinput.base.dir=$INPUT_BASE_DIR")
CMD+=("-Dinput.inventories.list=$INPUT_INVENTORIES_LIST")
CMD+=("-Doutput.inventories.dir=$OUTPUT_INVENTORIES_DIR")

echo "${CMD[@]}"
"${CMD[@]}"
