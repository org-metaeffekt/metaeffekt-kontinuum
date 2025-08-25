#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="advise/advise_create-dashboard-01.sh"

# Check if config.sh exists and source it
if [[ -f "$CONFIG_PATH" ]]; then
    source "$CONFIG_PATH"
else
    echo "Error: config.sh not found at $CONFIG_PATH" >&2
    exit 1
fi

CMD=(mvn -f "$PROCESSORS_DIR/advise/advise_create-dashboard.xml" process-resources)
CMD+=("-Dinput.inventory.file=$INPUT_INVENTORY_FILE")
CMD+=("-Doutput.dashboard.file=$OUTPUT_DASHBOARD_FILE")
CMD+=("-Denv.vulnerability.mirror.dir=$VULNERABILITY_MIRROR_DIR")
CMD+=("-Dinput.security.policy.file=$SECURITY_POLICY")


echo "${CMD[@]}"
"${CMD[@]}"
