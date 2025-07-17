#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="util_aggregate-sources-01.sh"

# Check if config.sh exists and source it
if [[ -f "$CONFIG_PATH" ]]; then
    source "$CONFIG_PATH"
else
    echo "Error: config.sh not found at $CONFIG_PATH" >&2
    exit 1
fi

# Parse command line options
while getopts "c:" flag; do
    case "$flag" in
        c) CASE="$OPTARG" ;;
        *) cat <<EOF
            Usage: $0 [options]
                -c <case>   : which case to select for running the test. Either an absolute path or relative to the CASES_DIR, defined in the config.sh
                -h          : show this help message

            EXAMPLE:
                ./script.sh -c 1
EOF
    esac
done

# Check if selected case exists
if [ -f "$CASES_DIR/$CASE" ]; then
   # shellcheck disable=SC1090
   source "$CASES_DIR/$CASE"
elif [ -f "$CASE" ]; then
   # shellcheck disable=SC1090
   source "$CASE"
else
    echo "Error: Case [$CASE] does not exist. Has to be either relative to [$CASES_DIR] or as an absolute path."
    exit 1
fi

# Run maven command
CMD=(mvn -f "$PROCESSORS_DIR/util_aggregate-sources.xml" process-resources)
CMD+=("-Dinput.inventory.file=$INPUT_INVENTORY_FILE")
CMD+=("-Dtarget.dir=$TARGET_DIR")
CMD+=("-Dannex.source.dir=$ANNEX_SOURCE_DIR")
CMD+=("-Dretained.sources.dir=$RETAINED_SOURCES_DIR")
CMD+=("-Dalternative.artifact.source.mapping=$ALTERNATIVE_ARTIFACT_SOURCE_MAPPING")
CMD+=("-Dinclude.all.sources=$INCLUDE_ALL_SOURCES")
CMD+=("-Dfail.on.missing.sources=$FAIL_ON_MISSING_SOURCES")

echo "${CMD[@]}"
"${CMD[@]}"
