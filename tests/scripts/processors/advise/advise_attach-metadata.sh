#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="advise/advise_attach-metadata-01.sh"

# Check if config.sh exists and source it
if [[ -f "$CONFIG_PATH" ]]; then
    source "$CONFIG_PATH"
else
    echo "Error: config.sh not found at $CONFIG_PATH" >&2
    exit 1
fi

# Run maven command
CMD=(mvn -f "$PROCESSORS_DIR/advise/advise_attach-metadata.xml" process-resources)
CMD+=("-Dinput.inventory.file=$INPUT_INVENTORY_FILE")
CMD+=("-Doutput.inventory.file=$OUTPUT_INVENTORY_FILE")
CMD+=("-Dparam.metadata.asset.id=$METADATA_ASSET_ID")
CMD+=("-Dparam.metadata.asset.name=$METADATA_ASSET_NAME")
CMD+=("-Dparam.metadata.asset.version=$METADATA_ASSET_VERSION")
CMD+=("-Dparam.metadata.asset.path=$METADATA_ASSET_PATH")
CMD+=("-Dparam.metadata.asset.type=$METADATA_ASSET_TYPE")

echo "${CMD[@]}"
"${CMD[@]}"