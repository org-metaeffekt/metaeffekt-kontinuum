#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="util/util_merge-filter-01.sh"

# Check if config.sh exists and source it
if [[ -f "$CONFIG_PATH" ]]; then
    source "$CONFIG_PATH"
else
    echo "Error: config.sh not found at $CONFIG_PATH" >&2
    exit 1
fi

# Run maven command
CMD=(mvn -f "$PROCESSORS_DIR/util/util_merge-filter.xml" process-resources)
CMD+=("-Dinput.reference.inventories.dir=$INPUT_REFERENCE_INVENTORIES_DIR")
CMD+=("-Doutput.filtered.inventory.file=$OUTPUT_FILTERED_INVENTORY_FILE")
CMD+=("-Dparam.vulnerability.advisory.filter=$PARAM_VULNERABILITY_ADVISORY_FILTER")
CMD+=("-Dinput.security.policy.file=$INPUT_SECURITY_POLICY_FILE")

echo "${CMD[@]}"
"${CMD[@]}"
