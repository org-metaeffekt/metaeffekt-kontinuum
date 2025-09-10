#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="util/util_update-mirror-01.sh"

# Check if config.sh exists and source it
check_shared_config() {
  if [[ -f "$CONFIG_PATH" ]]; then
      source "$CONFIG_PATH"
  else
      echo "Error: config.sh not found at $CONFIG_PATH" >&2
      exit 1
  fi
}

# Run maven command
run_maven_command() {
  CMD=(mvn -f "$PROCESSORS_DIR/util/util_update-mirror.xml" compile -P withoutProxy)
  CMD+=("-Doutput.vulnerability.mirror.dir=$MIRROR_TARGET_DIR")
  CMD+=("-Dparam.mirror.archive.url=$MIRROR_ARCHIVE_URL")
  CMD+=("-Dparam.mirror.archive.name=$MIRROR_ARCHIVE_NAME")

  echo "${CMD[@]}"
  "${CMD[@]}"
}

main() {
  check_shared_config
  run_maven_command

  rm -r "$PROCESSORS_DIR/util/target" # Necessary because antrun produces a target folder in processors
}

main "$@"