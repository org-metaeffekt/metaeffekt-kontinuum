#!/bin/bash

set -eo pipefail

########################################
# Configuration and Directory Setup
########################################

readonly SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
readonly KONTINUUM_DIR="$(realpath "$SELF_DIR/../../../")"

# Export environment variables
export KONTINUUM_DIR
export PROCESSORS_DIR="$KONTINUUM_DIR/processors"
export TESTS_DIR="$KONTINUUM_DIR/tests"
export GENERIC_RESOURCES_DIR="$TESTS_DIR/resources/generic"
export WORKBENCH_DIR="$TESTS_DIR/resources/workbench"
export WORKSPACE_DIR="$TESTS_DIR/resources/workspace-001"
export PRODUCT_DIR="$WORKSPACE_DIR/sample-product-1.0.0"
export CASES_DIR="$TESTS_DIR/scripts/cases"

# Target directory structure
readonly TARGET_DIR="$TESTS_DIR/target"
readonly TARGET_WORKSPACE_DIR="$TARGET_DIR/workspace-001"
readonly TARGET_PRODUCT_DIR="$TARGET_WORKSPACE_DIR/sample-product-1.0.0"

readonly ANALYZED_DIR="$TARGET_PRODUCT_DIR/02_analyzed"
readonly RESOLVED_DIR="$TARGET_PRODUCT_DIR/03_resolved"
readonly ADVISED_DIR="$TARGET_PRODUCT_DIR/05_advised"
readonly SCANNED_DIR="$TARGET_PRODUCT_DIR/04_scanned"
readonly REPORTED_DIR="$TARGET_PRODUCT_DIR/06_reported"
readonly UTIL_DIR="$TARGET_PRODUCT_DIR/00_util"
readonly PORTFOLIO_DIR="$TARGET_PRODUCT_DIR/00_portfolio"
readonly CONVERTED_DIR="$TARGET_PRODUCT_DIR/00_converted"

########################################
# Function Definitions
########################################

# Print error message and exit
error_exit() {
    echo "Error: $1" >&2
    exit 1
}

# Print usage information
print_usage() {
    cat << EOF
Usage: $0 [options]
    -c <case>   : Which case to select for running the test. Either an absolute
                  path or relative to the CASES_DIR ($CASES_DIR)
    -h          : Show this help message

Example:
    $0 -c /path/to/case
EOF
}

# Initialize target directory structure
initialize_target_directories() {
    echo "Creating target directories if missing..."

    if [[ ! -d "$TARGET_DIR" ]]; then
        echo "Initializing target directory structure..."
        mkdir -p "$TARGET_DIR"
        cp -r "$TESTS_DIR/resources/workspace-001" "$TARGET_DIR"
    fi

    local directories=(
        "$CONVERTED_DIR"
        "$ANALYZED_DIR"
        "$RESOLVED_DIR"
        "$ADVISED_DIR"
        "$SCANNED_DIR"
        "$REPORTED_DIR"
        "$UTIL_DIR"
        "$PORTFOLIO_DIR"
    )

    if ! mkdir -p "${directories[@]}"; then
        error_exit "Failed to create target directories"
    fi
}

# Load case configuration
load_case_configuration() {
    local case_path="$1"

    if [[ -f "$CASES_DIR/$case_path" ]]; then
        source "$CASES_DIR/$case_path"
    elif [[ -f "$case_path" ]]; then
        source "$case_path"
    else
        error_exit "Case [$case_path] does not exist. Must be either relative to [$CASES_DIR] or an absolute path."
    fi
}

########################################
# Main Script Execution
########################################

main() {
    while getopts "c:h" flag; do
        case "$flag" in
            c) CASE="$OPTARG" ;;
            h) print_usage; exit 0 ;;
            *) print_usage; exit 1 ;;
        esac
    done

    initialize_target_directories
    load_case_configuration "$CASE"

    echo "Configuration loaded successfully"
}

main "$@"