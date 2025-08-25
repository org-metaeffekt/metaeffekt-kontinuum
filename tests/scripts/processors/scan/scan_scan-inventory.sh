#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="scan/scan_scan-inventory-01.sh"

# Check if config.sh exists and source it
if [[ -f "$CONFIG_PATH" ]]; then
    source "$CONFIG_PATH"
else
    echo "Error: config.sh not found at $CONFIG_PATH" >&2
    exit 1
fi



# Run maven command
CMD=(mvn -f "$PROCESSORS_DIR/scan/scan_scan-inventory.xml" process-resources)
CMD+=("-Dinput.inventory.file=$INPUT_INVENTORY_FILE")
CMD+=("-Doutput.inventory.file=$OUTPUT_INVENTORY_FILE")
CMD+=("-Dinput.output.analysis.base.dir=$ANALYSIS_BASE_DIR")
CMD+=("-Dinput.properties.file=$PROPERTIES_FILE")

echo "${CMD[@]}"
"${CMD[@]}"
