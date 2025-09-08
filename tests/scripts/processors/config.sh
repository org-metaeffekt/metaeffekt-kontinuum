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
export CASES_DIR="$TESTS_DIR/scripts/cases"

# Target directory structure
readonly TARGET_DIR="$TESTS_DIR/target"


# Workspace 001
export WORKSPACE_001_DIR="$TESTS_DIR/resources/workspace-001"

readonly TARGET_WORKSPACE_001_DIR="$TARGET_DIR/workspace-001"
readonly ANALYZED_DIR_001="$TARGET_WORKSPACE_001_DIR/sample-product-1.0.0/02_analyzed"
readonly RESOLVED_DIR_001="$TARGET_WORKSPACE_001_DIR/sample-product-1.0.0/03_resolved"
readonly ADVISED_DIR_001="$TARGET_WORKSPACE_001_DIR/sample-product-1.0.0/05_advised"
readonly SCANNED_DIR_001="$TARGET_WORKSPACE_001_DIR/sample-product-1.0.0/04_scanned"
readonly REPORTED_DIR_001="$TARGET_WORKSPACE_001_DIR/sample-product-1.0.0/06_reported"
readonly UTIL_DIR_001="$TARGET_WORKSPACE_001_DIR/sample-product-1.0.0/00_util"
readonly PORTFOLIO_DIR_001="$TARGET_WORKSPACE_001_DIR/sample-product-1.0.0/00_portfolio"
readonly CONVERTED_DIR_001="$TARGET_WORKSPACE_001_DIR/sample-product-1.0.0/00_converted"

#Workspace 002
export WORKSPACE_002_DIR="$TESTS_DIR/resources/workspace-002"

readonly TARGET_WORKSPACE_002_DIR="$TARGET_DIR/workspace-002"
readonly ANALYZED_DIR_002="$TARGET_WORKSPACE_002_DIR/sample-product-1.0.0/02_analyzed"
readonly RESOLVED_DIR_002="$TARGET_WORKSPACE_002_DIR/sample-product-1.0.0/03_resolved"
readonly ADVISED_DIR_002="$TARGET_WORKSPACE_002_DIR/sample-product-1.0.0/05_advised"
readonly SCANNED_DIR_002="$TARGET_WORKSPACE_002_DIR/sample-product-1.0.0/04_scanned"
readonly REPORTED_DIR_002="$TARGET_WORKSPACE_002_DIR/sample-product-1.0.0/06_reported"
readonly UTIL_DIR_002="$TARGET_WORKSPACE_002_DIR/sample-product-1.0.0/00_util"
readonly PORTFOLIO_DIR_002="$TARGET_WORKSPACE_002_DIR/sample-product-1.0.0/00_portfolio"
readonly CONVERTED_DIR_002="$TARGET_WORKSPACE_002_DIR/sample-product-1.0.0/00_converted"


########################################
# Function Definitions
########################################


# Print usage information
print_usage() {
    cat << EOF
Usage: $0 [options]
    -c <case>   : Which case to select for running the test. Either an absolute
                  path or relative to the CASES_DIR ($CASES_DIR)
    -l          : Provides the log level, can either be ALL, IO_ONLY, CMD_ONLY or INFO
    -p          : Enables pipeline mode, set this flag only if running multiple scripts in sequence
    -h          : Show this help message

Example:
    $0 -c /path/to/case
EOF
}

# Initialize target directory structure
initialize_target_directories() {
    if [[ ! -d "$TARGET_DIR" ]]; then
        mkdir -p "$TARGET_DIR"
        cp -r "$WORKSPACE_001_DIR" "$TARGET_DIR"
        cp -r "$WORKSPACE_002_DIR" "$TARGET_DIR"
    fi

    local directories=(
        "$CONVERTED_DIR_001"
        "$ANALYZED_DIR_001"
        "$RESOLVED_DIR_001"
        "$ADVISED_DIR_001"
        "$SCANNED_DIR_001"
        "$REPORTED_DIR_001"
        "$UTIL_DIR_001"
        "$PORTFOLIO_DIR_001"
        "$CONVERTED_DIR_002"
        "$ANALYZED_DIR_002"
        "$RESOLVED_DIR_002"
        "$ADVISED_DIR_002"
        "$SCANNED_DIR_002"
        "$REPORTED_DIR_002"
        "$UTIL_DIR_002"
        "$PORTFOLIO_DIR_002"
    )
}

########################################
# Main Script Execution
########################################

main() {
    initialize_target_directories
}

main "$@"