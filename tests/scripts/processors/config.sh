#!/bin/bash

set -eo pipefail

########################################
# Configuration and Directory Setup
########################################
readonly SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
KONTINUUM_DIR="$(realpath "$SELF_DIR/../../../")"

# Export environment variables

export KONTINUUM_DIR
export PROCESSORS_DIR="$KONTINUUM_DIR/processors"
export TESTS_DIR="$KONTINUUM_DIR/tests"
export GENERIC_RESOURCES_DIR="$TESTS_DIR/resources/generic"
export INTERNAL_WORKBENCH_DIR="$TESTS_DIR/resources/workbench"
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
    -l          : Provides the log level, can either be ALL, CONFIG, CMD, INFO or ERROR
    -f          : In which file to log all information
    -h          : Show this help message
    -o          : If set, outputs the logs on the console

Example:
    $0 -c /path/to/case -l CMD -o
EOF
}

# Initialize target directory structure
initialize_target_directories() {
    log_info "Creating target directories if missing"

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

    if ! mkdir -p "${directories[@]}"; then
      log_error "Failed to create target directories"
    else
      log_info "Target directories successfully created"
    fi
}

source_case_file() {
    local case_file="$1"
    if [[ -f "$CASES_DIR/$case_file" ]]; then
        source "$CASES_DIR/$case_file"
        log_info "Successfully sourced case file $(realpath "$CASES_DIR/$case_file")"
    elif [[ -f "$case_file" ]]; then
        source "$case_file"
        log_info "Successfully sourced case file $(realpath "$case_file")"
    else
        log_error "Case [$case_file] does not exist. Must be either relative to [$CASES_DIR] or an absolute path."
        exit 1
    fi
}

load_externalrc() {
  if [ -f "$KONTINUUM_DIR/external.rc" ]; then
      source "$KONTINUUM_DIR/external.rc"
  else
      log_error "Missing external.rc file in root of repository."
      exit 1
  fi

  if [ -n "$EXTERNAL_WORKBENCH_DIR" ]; then
      export WORKBENCH_DIR="$EXTERNAL_WORKBENCH_DIR"
      log_info "Found workbench repository at $EXTERNAL_WORKBENCH_DIR"
  else
    log_error "Could not find workbench repository at path specified in the external.rc file"
  fi

  if [ -n "${EXTERNAL_VULNERABILITY_MIRROR_DIR:-}" ]; then
    log_info "Found external mirror at $EXTERNAL_VULNERABILITY_MIRROR_DIR"
  else
    EXTERNAL_VULNERABILITY_MIRROR_DIR="$KONTINUUM_DIR/.mirror"
    log_info "No external mirror specified, switching to internal kontinuum mirror."
  fi

  if [ -n "${EXTERNAL_VULNERABILITY_MIRROR_URL:-}" ]; then
      log_info "External mirror URL specified: $EXTERNAL_VULNERABILITY_MIRROR_URL"
  else
    EXTERNAL_VULNERABILITY_MIRROR_URL="http://ae-scanner/mirror/index/index-database_legacy.zip"
    log_info "No external mirror URL specified. Using either mirror specified in external.rc file or repository-specific local mirror if exists."
  fi
}

########################################
# Main Script Execution
########################################

main() {
  load_externalrc
  initialize_target_directories
}

main "$@"