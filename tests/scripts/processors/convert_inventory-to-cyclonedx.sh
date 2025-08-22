#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="convert_inventory-to-cyclonedx-01.sh"

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
                ./script.sh -c /path/to/case
EOF
    esac
done

# Check if selected case exists
if [ -f "$CASES_DIR/$CASE" ]; then
   source "$CASES_DIR/$CASE"
elif [ -f "$CASE" ]; then
   source "$CASE"
else
    echo "Error: Case [$CASE] does not exist. Has to be either relative to [$CASES_DIR] or as an absolute path."
    exit 1
fi

CMD=(mvn -f "$PROCESSORS_DIR/convert_inventory-to-cyclonedx.xml" process-resources)
CMD+=("-Dinput.inventory.file=$INPUT_INVENTORY_FILE")
CMD+=("-Ddocument.name=$DOCUMENT_NAME")
CMD+=("-Ddocument.description=$DOCUMENT_DESCRIPTION")
CMD+=("-Ddocument.organization=$DOCUMENT_ORGANIZATION")
CMD+=("-Ddocument.organization.url=$DOCUMENT_ORGANIZATION_URL")
CMD+=("-Doutput.bom.file=$OUTPUT_BOM_FILE")
CMD+=("-Doutput.format=$OUTPUT_FORMAT")

echo "${CMD[@]}"
"${CMD[@]}"