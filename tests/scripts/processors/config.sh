#!/bin/bash

set -euo pipefail

# Define base directories
SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
export KONTINUUM_DIR=$(realpath "$SELF_DIR/../../../")
export PROCESSORS_DIR="$KONTINUUM_DIR/processors"
export TESTS_DIR="$KONTINUUM_DIR/tests"
export RESOURCES_DIR="$TESTS_DIR/resources/generic"
export CASES_DIR="$TESTS_DIR/scripts/cases"

TARGET_DIR="$TESTS_DIR/target"
ANALYZED_DIR="$TARGET_DIR/analyzed"
RESOLVED_DIR="$TARGET_DIR/resolved"
ADVISED_DIR="$TARGET_DIR/advised"
SCANNED_DIR="$TARGET_DIR/scanned"
REPORTED_DIR="$TARGET_DIR/reported"
UTIL_DIR="$TARGET_DIR/util"
PORTFOLIO_DIR="$TARGET_DIR/portfolio"
CONVERTED_DIR="$TARGET_DIR/converted"

# Create all target directories
echo "Creating target directories..."
if ! mkdir -p "$CONVERTED_DIR" "$ANALYZED_DIR" "$RESOLVED_DIR" "$ADVISED_DIR" "$SCANNED_DIR" "$REPORTED_DIR" "$UTIL_DIR"; then
    echo "Error: Failed to create target directories" >&2
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

echo "Configuration loaded successfully"