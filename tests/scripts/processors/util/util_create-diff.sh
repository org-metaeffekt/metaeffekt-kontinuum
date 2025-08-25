#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="util/util_create-diff-01.sh"

# Check if config.sh exists and source it
if [[ -f "$CONFIG_PATH" ]]; then
    source "$CONFIG_PATH"
else
    echo "Error: config.sh not found at $CONFIG_PATH" >&2
    exit 1
fi


# Run maven command
CMD=(mvn -f "$PROCESSORS_DIR/util/util_create-diff.xml" process-resources)
CMD+=("-Dinput.inventory.file=$INPUT_INVENTORY_FILE")
CMD+=("-Dinput.inventory.compare.file=$INPUT_INVENTORY_COMPARE_FILE")
CMD+=("-Dparam.inventory.version=$INVENTORY_VERSION")
CMD+=("-Dparam.inventory.compare.version=$INVENTORY_COMPARE_VERSION")
CMD+=("-Doutput.inventory.dir=$OUTPUT_INVENTORY_DIR")
CMD+=("-Dinput.security.policy.file=$SECURITY_POLICY_FILE")


echo "${CMD[@]}"
"${CMD[@]}"