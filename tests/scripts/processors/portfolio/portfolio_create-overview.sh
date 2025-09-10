#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="portfolio/portfolio_create-overview-01.sh"

# Check if config.sh exists and source it
check_shared_config() {
  if [[ -f "$CONFIG_PATH" ]]; then
      source "$CONFIG_PATH"
  else
      echo "Error: config.sh not found at $CONFIG_PATH" >&2
      exit 1
  fi
}

#Run maven command
run_maven_command() {
  CMD=(mvn -f "$PROCESSORS_DIR/portfolio/portfolio_create-overview.xml" process-resources -X)
  CMD+=("-Dinput.inventory.dir=$INPUT_INVENTORY_DIR")
  CMD+=("-Dinput.inventory.path=$INPUT_INVENTORY_PATH")
  CMD+=("-Dinput.security.policy.file=$INPUT_SECURITY_POLICY_FILE")
  CMD+=("-Dinput.advisor.inventories.dir=$INPUT_ADVISOR_INVENTORIES_DIR")
  CMD+=("-Dinput.dashboards.dir=$INPUT_DASHBOARDS_DIR")
  CMD+=("-Dinput.reports.dir=$INPUT_REPORTS_DIR")
  CMD+=("-Doutput.overview.file=$OUTPUT_OVERVIEW_FILE")

  echo "${CMD[@]}"
  "${CMD[@]}"
}

main() {
  check_shared_config
  run_maven_command
}

main "$@"