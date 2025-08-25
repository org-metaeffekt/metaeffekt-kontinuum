#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="util/util_merge-inventories-01.sh"

# Check if config.sh exists and source it
if [[ -f "$CONFIG_PATH" ]]; then
    source "$CONFIG_PATH"
else
    echo "Error: config.sh not found at $CONFIG_PATH" >&2
    exit 1
fi



# Run maven command
CMD=(mvn -f "$PROCESSORS_DIR/util/util_merge-inventories.xml" process-resources)
CMD+=("-Dinput.inventory.dir=$INPUT_INVENTORY_DIR")
CMD+=("-Dparam.inventory.includes=$INVENTORY_INCLUDES")
CMD+=("-Doutput.inventory.file=$OUTPUT_INVENTORY_FILE")

echo "${CMD[@]}"
"${CMD[@]}"
