#!/bin/bash

set -eo pipefail

########################################
# Configuration and Directory Setup
########################################
readonly PRELOAD_SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
KONTINUUM_ROOT="$(cd "$PRELOAD_SELF_DIR/../../.." && pwd)"
HARNESS_ROOT="$(cd "$PRELOAD_SELF_DIR/../../../.." && pwd)"
KONTINUUM_DIR="$KONTINUUM_ROOT"

if [ -f "$PRELOAD_SELF_DIR/log.sh" ]; then
  source "$PRELOAD_SELF_DIR/log.sh"
fi

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

readonly TARGET_WORKSPACE_001_DIR="$TARGET_DIR/workspace-001/sample-product-1.0.0"
readonly ADDITIONAL_DIR_001="$TARGET_WORKSPACE_001_DIR/xx_additional"
readonly FETCHED_DIR_001="$TARGET_WORKSPACE_001_DIR/00_fetched"
readonly EXTRACTED_DIR_001="$TARGET_WORKSPACE_001_DIR/01_extracted"
readonly PREPARED_DIR_001="$TARGET_WORKSPACE_001_DIR/02_prepared"
readonly AGGREGATED_DIR_001="$TARGET_WORKSPACE_001_DIR/03_aggregated"
readonly RESOLVED_DIR_001="$TARGET_WORKSPACE_001_DIR/04_resolved"
readonly SCANNED_DIR_001="$TARGET_WORKSPACE_001_DIR/05_scanned"
readonly ADVISED_DIR_001="$TARGET_WORKSPACE_001_DIR/06_advised"
readonly GROUPED_DIR_001="$TARGET_WORKSPACE_001_DIR/07_grouped"
readonly REPORTED_DIR_001="$TARGET_WORKSPACE_001_DIR/08_reported"
readonly SUMMARIZED_DIR_001="$TARGET_WORKSPACE_001_DIR/09_summarized"
readonly PORTFOLIO_DIR_001="$ADDITIONAL_DIR_001/portfolio"
readonly CONVERTED_DIR_001="$ADDITIONAL_DIR_001/converted"

#Workspace 002
export WORKSPACE_002_DIR="$TESTS_DIR/resources/workspace-002"

readonly TARGET_WORKSPACE_002_DIR="$TARGET_DIR/workspace-002/sample-product-1.0.0"
readonly ADDITIONAL_DIR_002="$TARGET_WORKSPACE_002_DIR/xx_additional"
readonly FETCHED_DIR_002="$TARGET_WORKSPACE_002_DIR/00_fetched"
readonly EXTRACTED_DIR_002="$TARGET_WORKSPACE_002_DIR/01_extracted"
readonly PREPARED_DIR_002="$TARGET_WORKSPACE_002_DIR/02_prepared"
readonly AGGREGATED_DIR_002="$TARGET_WORKSPACE_002_DIR/03_aggregated"
readonly RESOLVED_DIR_002="$TARGET_WORKSPACE_002_DIR/04_resolved"
readonly SCANNED_DIR_002="$TARGET_WORKSPACE_002_DIR/05_scanned"
readonly ADVISED_DIR_002="$TARGET_WORKSPACE_002_DIR/06_advised"
readonly GROUPED_DIR_002="$TARGET_WORKSPACE_002_DIR/07_grouped"
readonly REPORTED_DIR_002="$TARGET_WORKSPACE_002_DIR/08_reported"
readonly SUMMARIZED_DIR_002="$TARGET_WORKSPACE_002_DIR/09_summarized"
readonly PORTFOLIO_DIR_002="$ADDITIONAL_DIR_002/portfolio"
readonly CONVERTED_DIR_002="$ADDITIONAL_DIR_002/converted"

#Workspace 003
export WORKSPACE_003_DIR="$TESTS_DIR/resources/workspace-003"

readonly TARGET_WORKSPACE_003_DIR="$TARGET_DIR/workspace-003/sample-product-1.0.0"
readonly PREPARED_DIR_003="$TARGET_WORKSPACE_003_DIR/02_prepared"


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
        "$ADDITIONAL_DIR_001"
        "$FETCHED_DIR_001"
        "$EXTRACTED_DIR_001"
        "$PREPARED_DIR_001"
        "$AGGREGATED_DIR_001"
        "$RESOLVED_DIR_001"
        "$SCANNED_DIR_001"
        "$ADVISED_DIR_001"
        "$GROUPED_DIR_001"
        "$REPORTED_DIR_001"
        "$SUMMARIZED_DIR_001"
        "$PORTFOLIO_DIR_001"
        "$CONVERTED_DIR_001"
        "$ADDITIONAL_DIR_002"
        "$FETCHED_DIR_002"
        "$EXTRACTED_DIR_002"
        "$PREPARED_DIR_002"
        "$AGGREGATED_DIR_002"
        "$RESOLVED_DIR_002"
        "$SCANNED_DIR_002"
        "$ADVISED_DIR_002"
        "$GROUPED_DIR_002"
        "$REPORTED_DIR_002"
        "$SUMMARIZED_DIR_002"
        "$PORTFOLIO_DIR_002"
        "$CONVERTED_DIR_002"
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

load_properties() {
  local prop_file="$1"
  [ ! -f "$prop_file" ] && return 1

  while IFS= read -r line || [ -n "$line" ]; do
    # Remove carriage return characters
    line="${line//$'\r'/}"
    # Strip leading whitespace
    line="${line#"${line%%[![:space:]]*}"}"
    # Strip trailing whitespace
    line="${line%"${line##*[![:space:]]}"}"

    # Skip comments and empty lines
    [[ -z "$line" || "$line" =~ ^[#!] ]] && continue

    # Parse key=val or key:val
    if [[ "$line" =~ ^([^=:]+)[=:](.*)$ ]]; then
      local key="${BASH_REMATCH[1]}"
      local val="${BASH_REMATCH[2]}"

      # Trim whitespace around key and value
      key="${key#"${key%%[![:space:]]*}"}"
      key="${key%"${key##*[![:space:]]}"}"
      val="${val#"${val%%[![:space:]]*}"}"
      val="${val%"${val##*[![:space:]]}"}"

      # Remove surrounding quotes if present
      if [[ "$val" =~ ^\"(.*)\"$ ]] || [[ "$val" =~ ^\'(.*)\'$ ]]; then
        val="${BASH_REMATCH[1]}"
      fi

      # Convert property key to uppercase variable name (dots and hyphens become underscores)
      local var_name
      var_name=$(echo "$key" | tr '[:lower:]' '[:upper:]' | tr '.-' '__')

      export "$var_name"="$val"
    fi
  done < "$prop_file"
}

load_project_properties() {
  local properties_file=""
  if [ -f "$KONTINUUM_ROOT/.local.properties" ]; then
    properties_file="$KONTINUUM_ROOT/.local.properties"
  elif [ -f "$HARNESS_ROOT/.local.properties" ]; then
    properties_file="$HARNESS_ROOT/.local.properties"
  fi

  if [ -n "$properties_file" ]; then
    load_properties "$properties_file"
    log_info "Successfully loaded .local.properties from $properties_file"
  else
    log_error "Terminating: .local.properties file not found in metaeffekt-kontinuum or encompassing integration harness."
    exit 1
  fi

  # Set aliases for Workbench directory
  if [ -n "${AE_WORKBENCH_DIR:-}" ] && [ -z "${EXTERNAL_WORKBENCH_DIR:-}" ]; then
    EXTERNAL_WORKBENCH_DIR="$AE_WORKBENCH_DIR"
  elif [ -n "${EXTERNAL_WORKBENCH_DIR:-}" ] && [ -z "${AE_WORKBENCH_DIR:-}" ]; then
    AE_WORKBENCH_DIR="$EXTERNAL_WORKBENCH_DIR"
  fi
  export EXTERNAL_WORKBENCH_DIR AE_WORKBENCH_DIR

  # Set aliases for Kontinuum directory
  if [ -n "${AE_KONTINUUM_DIR:-}" ] && [ -z "${EXTERNAL_KONTINUUM_DIR:-}" ]; then
    EXTERNAL_KONTINUUM_DIR="$AE_KONTINUUM_DIR"
  elif [ -n "${EXTERNAL_KONTINUUM_DIR:-}" ] && [ -z "${AE_KONTINUUM_DIR:-}" ]; then
    AE_KONTINUUM_DIR="$EXTERNAL_KONTINUUM_DIR"
  fi
  export EXTERNAL_KONTINUUM_DIR AE_KONTINUUM_DIR

  # Set aliases for Vulnerability Mirror directory
  if [ -n "${VULNERABILITY_MIRROR_DIR:-}" ] && [ -z "${EXTERNAL_VULNERABILITY_MIRROR_DIR:-}" ]; then
    EXTERNAL_VULNERABILITY_MIRROR_DIR="$VULNERABILITY_MIRROR_DIR"
  elif [ -n "${EXTERNAL_VULNERABILITY_MIRROR_DIR:-}" ] && [ -z "${VULNERABILITY_MIRROR_DIR:-}" ]; then
    VULNERABILITY_MIRROR_DIR="$EXTERNAL_VULNERABILITY_MIRROR_DIR"
  fi
  export EXTERNAL_VULNERABILITY_MIRROR_DIR VULNERABILITY_MIRROR_DIR

  # Set aliases for Vulnerability Mirror URL
  if [ -n "${VULNERABILITY_MIRROR_URL:-}" ] && [ -z "${EXTERNAL_VULNERABILITY_MIRROR_URL:-}" ]; then
    EXTERNAL_VULNERABILITY_MIRROR_URL="$VULNERABILITY_MIRROR_URL"
  elif [ -n "${EXTERNAL_VULNERABILITY_MIRROR_URL:-}" ] && [ -z "${VULNERABILITY_MIRROR_URL:-}" ]; then
    VULNERABILITY_MIRROR_URL="$EXTERNAL_VULNERABILITY_MIRROR_URL"
  fi
  export EXTERNAL_VULNERABILITY_MIRROR_URL VULNERABILITY_MIRROR_URL

  # Validate and log status
  if [ -n "${EXTERNAL_WORKBENCH_DIR:-}" ]; then
    log_info "Found external workbench at $EXTERNAL_WORKBENCH_DIR"
  else
    log_info "No ae.workbench.dir specified in .local.properties, this might result in scripts failing."
  fi

  if [ -n "${EXTERNAL_VULNERABILITY_MIRROR_DIR:-}" ]; then
    log_info "Found external mirror at $EXTERNAL_VULNERABILITY_MIRROR_DIR"
  else
    log_info "No vulnerability.mirror.dir specified in .local.properties, this might result in scripts failing."
  fi

  if [ -n "${EXTERNAL_VULNERABILITY_MIRROR_URL:-}" ]; then
    log_info "External mirror URL specified: $EXTERNAL_VULNERABILITY_MIRROR_URL"
  else
    log_info "No vulnerability.mirror.url specified in .local.properties, this might result in scripts failing."
  fi

  if [ -n "${AE_CORE_VERSION:-}" ]; then
    log_info "Core version specified: $AE_CORE_VERSION"
  else
    log_info "No ae.core.version specified in .local.properties file, using HEAD-SNAPSHOT."
    export AE_CORE_VERSION=HEAD-SNAPSHOT
  fi

  if [ -n "${AE_ARTIFACT_ANALYSIS_VERSION:-}" ]; then
    log_info "Artifact analysis version specified: $AE_ARTIFACT_ANALYSIS_VERSION"
  else
    log_info "No ae.artifact.analysis.version specified in .local.properties file, using HEAD-SNAPSHOT"
    export AE_ARTIFACT_ANALYSIS_VERSION=HEAD-SNAPSHOT
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
    load_project_properties
    initialize_target_directories
}

main "$@"