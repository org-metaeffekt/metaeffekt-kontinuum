#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="convert/convert_cyclonedx-to-inventory-01.sh"

# Check if config.sh exists and source it
if [[ -f "$CONFIG_PATH" ]]; then
    source "$CONFIG_PATH"
else
    echo "Error: config.sh not found at $CONFIG_PATH" >&2
    exit 1
fi

CMD=(mvn -f "$PROCESSORS_DIR/convert/convert_cyclonedx-to-inventory.xml" process-resources)
CMD+=("-Dinput.bom.file=$INPUT_BOM_FILE")
CMD+=("-Doutput.inventory.file=$OUTPUT_INVENTORY_FILE")

echo "${CMD[@]}"
"${CMD[@]}"