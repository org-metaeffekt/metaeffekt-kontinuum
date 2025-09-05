#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="advise/advise_generate-new-dashboard-01.sh"

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
  CMD=(mvn -f "$PROCESSORS_DIR/advise/advise_generate-new-dashboard.xml" process-resources)
  CMD+=("-Dinput.inventory.file=$INPUT_INVENTORY_FILE")
  CMD+=("-Dinput.security.policy.file=$SECURITY_POLICY")
  CMD+=("-Doutput.dashboard.file=$OUTPUT_DASHBOARD_FILE")
  CMD+=("-Dparam.timeline.conf.enabled=$PARAM_TIMELINE_CONF_ENABLED")
  CMD+=("-Denv.vulnerability.mirror.dir=$VULNERABILITY_MIRROR_DIR")
  CMD+=("-Dparam.timeline.max.threads=$PARAM_TIMELINE_MAX_THREADS")
  CMD+=("-Dparam.timeline.time.spent.max=$PARAM_TIMELINE_TIME_SPENT_MAX")
  CMD+=("-Dparam.timeline.vuln.providers.list=$PARAM_TIMELINE_VULN_PROVIDERS_LIST")

  echo "${CMD[@]}"
  "${CMD[@]}"
}

main() {
  check_shared_config
  run_maven_command
}

main "$@"