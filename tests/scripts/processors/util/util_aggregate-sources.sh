#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="util/util_aggregate-sources-01.sh"

# Check if config.sh exists and source it
if [[ -f "$CONFIG_PATH" ]]; then
    source "$CONFIG_PATH"
else
    echo "Error: config.sh not found at $CONFIG_PATH" >&2
    exit 1
fi



# Run maven command
CMD=(mvn -f "$PROCESSORS_DIR/util/util_aggregate-sources.xml" process-resources)
CMD+=("-Dinput.inventory.file=$INPUT_INVENTORY_FILE")
CMD+=("-Dtarget.dir=$TARGET_DIR")
CMD+=("-Dannex.source.dir=$ANNEX_SOURCE_DIR")
CMD+=("-Dretained.sources.dir=$RETAINED_SOURCES_DIR")
CMD+=("-Dalternative.artifact.source.mapping=$ALTERNATIVE_ARTIFACT_SOURCE_MAPPING")
CMD+=("-Dinclude.all.sources=$INCLUDE_ALL_SOURCES")
CMD+=("-Dfail.on.missing.sources=$FAIL_ON_MISSING_SOURCES")

echo "${CMD[@]}"
"${CMD[@]}"
