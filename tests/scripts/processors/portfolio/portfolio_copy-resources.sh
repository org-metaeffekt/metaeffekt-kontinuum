#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="portfolio/portfolio_copy-resources-01.sh"

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
  CMD=(mvn -f "$PROCESSORS_DIR/portfolio/portfolio_copy-resources.xml" process-resources)
  CMD+=("-Dinput.inventories.dir=$INPUT_INVENTORIES_DIR")
  CMD+=("-Dinput.dashboards.dir=$INPUT_DASHBOARDS_DIR")
  CMD+=("-Dinput.reports.dir=$INPUT_REPORTS_DIR")
  CMD+=("-Dinput.advisor.inventories.dir=$INPUT_ADVISOR_INVENTORIES_DIR")
  CMD+=("-Doutput.resources.dir=$OUTPUT_RESOURCES_DIR")

  echo "${CMD[@]}"
  "${CMD[@]}"
}

main() {
  check_shared_config
  run_maven_command
}

main "$@"