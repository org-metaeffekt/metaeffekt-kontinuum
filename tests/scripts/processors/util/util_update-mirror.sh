#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="util/util_update-mirror-01.sh"

check_shared_config() {
  if [[ -f "$CONFIG_PATH" ]]; then
      source "$CONFIG_PATH"
  fi
}

# Run maven command
run_maven_command() {
  CMD=(mvn -f "$PROCESSORS_DIR/util/util_update-mirror.xml" compile -P withoutProxy)
  CMD+=("-Doutput.vulnerability.mirror.dir=$MIRROR_TARGET_DIR")
  CMD+=("-Dparam.mirror.archive.url=$MIRROR_ARCHIVE_URL")
  CMD+=("-Dparam.mirror.archive.name=$MIRROR_ARCHIVE_NAME")

  log_info "Running processor $PROCESSORS_DIR/util/util_update-mirror.xml"

  log_io "[]" "[
    output.vulnerability.mirror.dir=$MIRROR_TARGET_DIR
    param.mirror.archive.url=$MIRROR_ARCHIVE_URL
    param.mirror.archive.name=$MIRROR_ARCHIVE_NAME
  ]"

  log_mvn "${CMD[@]}"

  if ! "${CMD[@]}"; then
      log_error "Failed to run $PROCESSORS_DIR/util/util_update-mirror.xml because the maven execution was unsuccessful"
      return 1
  fi

  log_info "Successfully ran $PROCESSORS_DIR/util/util_update-mirror.xml"
}

main() {
  check_shared_config

  local case_file="$CASE"
  local log_level="ALL"
  local initialize_logging=true

  while getopts "c:h:l:p" flag; do
            case "$flag" in
                c) case_file="$OPTARG" ;;
                h) print_usage; exit 0 ;;
                l) log_level="$OPTARG" ;;
                p) initialize_logging=false ;;
                *) print_usage; exit 1 ;;
            esac
      done

  if [ $initialize_logging ]; then
    source "$TESTS_DIR/scripts/processors/log.sh"
    init_logging "$KONTINUUM_DIR/.logs/util_update-mirror.log" "$log_level"
  fi
  # No logging before this point


  if [[ -f "$CASES_DIR/$case_file" ]]; then
      source "$CASES_DIR/$case_file"
      log_info "Successfully sourced case file $CASES_DIR/$case_file"
  elif [[ -f "$case_file" ]]; then
      source "$case_file"
      log_info "Successfully sourced case file $case_file"
  else
      log_error "Case [$case_file] does not exist. Must be either relative to [$CASES_DIR] or an absolute path."
  fi

  run_maven_command
  rm -r "$PROCESSORS_DIR/util/target" # Necessary because antrun produces a target folder in processors

}

main "$@"