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
readonly AGGREGATED_DIR_001="$TARGET_WORKSPACE_001_DIR/sample-product-1.0.0/03_aggregated"
readonly RESOLVED_DIR_001="$TARGET_WORKSPACE_001_DIR/sample-product-1.0.0/04_resolved"
readonly ADVISED_DIR_001="$TARGET_WORKSPACE_001_DIR/sample-product-1.0.0/05_advised"
readonly SCANNED_DIR_001="$TARGET_WORKSPACE_001_DIR/sample-product-1.0.0/06_scanned"
readonly REPORTED_DIR_001="$TARGET_WORKSPACE_001_DIR/sample-product-1.0.0/07_reported"
readonly UTIL_DIR_001="$TARGET_WORKSPACE_001_DIR/sample-product-1.0.0/00_util"
readonly PORTFOLIO_DIR_001="$TARGET_WORKSPACE_001_DIR/sample-product-1.0.0/00_portfolio"
readonly CONVERTED_DIR_001="$TARGET_WORKSPACE_001_DIR/sample-product-1.0.0/00_converted"
readonly PREPARED_DIR_001="$TARGET_WORKSPACE_001_DIR/sample-product-1.0.0/01_prepared"
readonly EXTRACTED_DIR_001="$TARGET_WORKSPACE_001_DIR/sample-product-1.0.0/02_extracted"

#Workspace 002
export WORKSPACE_002_DIR="$TESTS_DIR/resources/workspace-002"

readonly TARGET_WORKSPACE_002_DIR="$TARGET_DIR/workspace-002"
readonly AGGREGATED_DIR_002="$TARGET_WORKSPACE_002_DIR/sample-product-1.0.0/03_aggregated"
readonly RESOLVED_DIR_002="$TARGET_WORKSPACE_002_DIR/sample-product-1.0.0/04_resolved"
readonly ADVISED_DIR_002="$TARGET_WORKSPACE_002_DIR/sample-product-1.0.0/05_advised"
readonly SCANNED_DIR_002="$TARGET_WORKSPACE_002_DIR/sample-product-1.0.0/06_scanned"
readonly REPORTED_DIR_002="$TARGET_WORKSPACE_002_DIR/sample-product-1.0.0/07_reported"
readonly UTIL_DIR_002="$TARGET_WORKSPACE_002_DIR/sample-product-1.0.0/00_util"
readonly PORTFOLIO_DIR_002="$TARGET_WORKSPACE_002_DIR/sample-product-1.0.0/00_portfolio"
readonly CONVERTED_DIR_002="$TARGET_WORKSPACE_002_DIR/sample-product-1.0.0/00_converted"
readonly PREPARED_DIR_002="$TARGET_WORKSPACE_002_DIR/sample-product-1.0.0/01_prepared"
readonly EXTRACTED_DIR_002="$TARGET_WORKSPACE_002_DIR/sample-product-1.0.0/02_extracted"

#Workspace 003
export WORKSPACE_003_DIR="$TESTS_DIR/resources/workspace-003"

readonly TARGET_WORKSPACE_003_DIR="$TARGET_DIR/workspace-003"
readonly PREPARED_DIR_003="$TARGET_WORKSPACE_003_DIR/sample-product-1.0.0/01_prepared"


########################################
# Function Definitions
########################################

# Initialize target directory structure
initialize_target_directories() {
    if [[ ! -d "$TARGET_DIR" ]]; then
        mkdir -p "$TARGET_DIR"
        cp -r "$WORKSPACE_001_DIR" "$TARGET_DIR"
        cp -r "$WORKSPACE_002_DIR" "$TARGET_DIR"
        cp -r "$WORKSPACE_003_DIR" "$TARGET_DIR"
    fi

    local directories=(
        "$CONVERTED_DIR_001"
        "$AGGREGATED_DIR_001"
        "$RESOLVED_DIR_001"
        "$ADVISED_DIR_001"
        "$SCANNED_DIR_001"
        "$REPORTED_DIR_001"
        "$UTIL_DIR_001"
        "$PORTFOLIO_DIR_001"
        "$PREPARED_DIR_001"
        "$EXTRACTED_DIR_001"
        "$CONVERTED_DIR_002"
        "$AGGREGATED_DIR_002"
        "$RESOLVED_DIR_002"
        "$ADVISED_DIR_002"
        "$SCANNED_DIR_002"
        "$REPORTED_DIR_002"
        "$UTIL_DIR_002"
        "$PORTFOLIO_DIR_002"
        "$PREPARED_DIR_002"
        "$EXTRACTED_DIR_002"
        "$PREPARED_DIR_003"
    )

    if ! mkdir -p "${directories[@]}"; then
      log_error "Failed to create missing target directories."
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
        log_error "Failed to source case file: [$case_file]. The path must either be relative [$CASES_DIR] or an absolute path."
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

  if [ -z "${EXTERNAL_WORKBENCH_DIR:-}" ]; then
    log_info "No EXTERNAL_WORKBENCH_DIR specified in the external.rc file, this might result in scripts failing."
  fi

  if [ -z "${EXTERNAL_VULNERABILITY_MIRROR_DIR:-}" ]; then
    log_info "No EXTERNAL_VULNERABILITY_MIRROR_DIR specified in external.rc, this might result in scripts failing."
  fi

  if [ -z "${EXTERNAL_VULNERABILITY_MIRROR_URL:-}" ]; then
    log_info "No EXTERNAL_VULNERABILITY_MIRROR_URL specified in external.rc, this might result in scripts failing."
  fi

  if [ -z "${EXTERNAL_VULNERABILITY_MIRROR_NAME:-}" ]; then
    log_info "No EXTERNAL_VULNERABILITY_MIRROR_NAME specified in external.rc, this might result in scripts failing."
  fi

  if [ -z "${AE_CORE_VERSION:-}" ]; then
    log_info "No AE_CORE_VERSION specified in external.rc file, using HEAD-SNAPSHOT."
  fi

  if [ -z "${AE_ARTIFACT_ANALYSIS_VERSION:-}" ]; then
    log_info "No AE_ARTIFACT_ANALYSIS_VERSION specified in external.rc file, using HEAD-SNAPSHOT"
  fi
}

pass_command_info_to_logger() {
  local processor_name="$1"

  echo ""
  log_info "\033[36mRunning $processor_name"
  log_maven_params
  log_debug "${CMD[*]}"

  if "${CMD[@]}" 2>&1 | while IFS= read -r line; do log_debug "$line"; done; then
      log_info "Successfully ran $processor_name"
  else
      log_error "Failed to run $processor_name because the underlying maven call failed."
      return 1
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