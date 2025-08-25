#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="advise/advise_enrich-inventory-01.sh"

# Check if config.sh exists and source it
if [[ -f "$CONFIG_PATH" ]]; then
    source "$CONFIG_PATH"
else
    echo "Error: config.sh not found at $CONFIG_PATH" >&2
    exit 1
fi

# Run maven command
CMD=(mvn -f "$PROCESSORS_DIR/advise/advise_enrich-inventory.xml" process-resources)
CMD+=("-Dinput.inventory.file=$INPUT_INVENTORY_FILE")
CMD+=("-Doutput.inventory.file=$OUTPUT_INVENTORY_FILE")
CMD+=("-Denv.vulnerability.mirror.dir=$VULNERABILITY_MIRROR_DIR")
CMD+=("-Dinput.security.policy.file=$SECURITY_POLICY")
CMD+=("-Doutput.tmp.dir=$PROCESSOR_TMP_DIR")
CMD+=("-Dparam.activate.ghsa.correlation=$ACTIVATE_GHSA_CORRELATION")
CMD+=("-Dparam.activate.ghsa=$ACTIVATE_GHSA")
CMD+=("-Dinput.correlation.dir=$CORRELATION_DIR")
CMD+=("-Dinput.context.dir=$CONTEXT_DIR")
CMD+=("-Dinput.assessment.dir=$ASSESSMENT_DIR")

echo "${CMD[@]}"
"${CMD[@]}"