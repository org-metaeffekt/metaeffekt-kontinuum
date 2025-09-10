#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
LOGGER_PATH="$SCRIPT_DIR/../log.sh"
CASE="util/util_portfolio-transfer-01.sh"


check_shared_config() {
  if [[ -f "$CONFIG_PATH" ]]; then
      source "$CONFIG_PATH"
  else
      log_error "Config file not found at $CONFIG_PATH"
      exit 1
  fi
}

initialize_logger() {
    local log_level="$1"
    local console_output_enabled="$2"
    local log_file="$3"
    source $LOGGER_PATH
    logger_init "$log_level" "$log_file" "${console_output_enabled}"
}

create_required_directories() {
  if [ -d "$TARGET_DIR/portfolio-manager" ]; then
    rm -r "$TARGET_DIR/portfolio-manager"
  fi

  PORTFOLIO_MANAGER_JARS="$TARGET_DIR/portfolio-manager-jars"

  if [ ! -d "$PORTFOLIO_MANAGER_JARS" ];then
    mkdir -p "$PORTFOLIO_MANAGER_JARS"
  fi
}

run_portfolio_manager() {
  CMD=(mvn -f "$PROCESSORS_DIR/util/util_portfolio-download-jars.xml" process-resources)
  CMD+=("-Dinput.cli.dir=$PORTFOLIO_MANAGER_JARS")

  echo "${CMD[@]}"
  "${CMD[@]}"

  cd "$INTERNAL_WORKBENCH_DIR/configs/portfolio-manager/service"
  java -jar "$PORTFOLIO_MANAGER_JARS/ae-portfolio-manager-service-0.5.0.jar" &

  cd "$INTERNAL_WORKBENCH_DIR/configs/portfolio-manager/client"
  OUTPUT=$(java -jar "$PORTFOLIO_MANAGER_JARS/ae-portfolio-manager-cli-0.5.0-exec.jar" create-project A)
  JSON_OUTPUT=$(echo "$OUTPUT" | sed -n '/^{/,$p')
  ADMIN_TOKEN=$(echo "$JSON_OUTPUT" | jq -r '.body.adminToken')
  echo "ADMIN TOKEN: $ADMIN_TOKEN"
}

run_maven_command_portfolio_upload() {
  cd "$SCRIPT_DIR"

  CMD=(mvn -f "$PROCESSORS_DIR/util/util_portfolio-upload.xml" process-resources)
  CMD+=("-Dparam.portfolio.manager.url=$PORTFOLIO_MANAGER_URL")
  CMD+=("-Dparam.portfolio.manager.token=$ADMIN_TOKEN")
  CMD+=("-Dinput.keystore.config.file=$KEYSTORE_CONFIG_FILE")
  CMD+=("-Dinput.file=$INPUT_FILE")
  CMD+=("-Dinput.cli.dir=$PORTFOLIO_MANAGER_JARS")
  CMD+=("-Dinput.truststore.config.file=$TRUSTSTORE_CONFIG_FILE")
  CMD+=("-Dparam.truststore.password=$TRUSTSTORE_PASSWORD")
  CMD+=("-Dparam.product.name=$PRODUCT_NAME")
  CMD+=("-Dparam.product.version=$PRODUCT_VERSION")
  CMD+=("-Dparam.product.artifact.id=$PRODUCT_ARTIFACT_ID")
  CMD+=("-Dparam.keystore.password=$KEYSTORE_PASSWORD")

  log_info "Running processor $PROCESSORS_DIR/util/util_portfolio-upload.xml"

  log_config "input.keystore.config.file=$KEYSTORE_CONFIG_FILE
              input.file=$INPUT_FILE
              input.cli.dir=$PORTFOLIO_MANAGER_JARS
              input.truststore.config.file=$TRUSTSTORE_CONFIG_FILE" ""

  log_mvn "${CMD[*]}"

  if "${CMD[@]}" 2>&1 | while IFS= read -r line; do log_mvn "$line"; done; then
      log_info "Successfully ran $PROCESSORS_DIR/util/util_portfolio-upload.xml"
  else
      log_error "Failed to run $PROCESSORS_DIR/util/util_portfolio-upload.xml because the maven execution was unsuccessful"
      return 1
  fi
}

run_maven_command_portfolio_download() {
  cd "$SCRIPT_DIR"

  CMD=(mvn -f "$PROCESSORS_DIR/util/util_portfolio-download.xml" process-resources)
  CMD+=("-Dparam.portfolio.manager.url=$PORTFOLIO_MANAGER_URL")
  CMD+=("-Dparam.portfolio.manager.token=$ADMIN_TOKEN")
  CMD+=("-Dinput.keystore.config.file=$KEYSTORE_CONFIG_FILE")
  CMD+=("-Dparam.keystore.password=$KEYSTORE_PASSWORD")
  CMD+=("-Dinput.truststore.config.file=$TRUSTSTORE_CONFIG_FILE")
  CMD+=("-Dparam.truststore.password=$TRUSTSTORE_PASSWORD")
  CMD+=("-Dparam.product.name=$PRODUCT_NAME")
  CMD+=("-Dparam.product.version=$PRODUCT_VERSION")
  CMD+=("-Dparam.product.artifact.id=$PRODUCT_ARTIFACT_ID")
  CMD+=("-Doutput.inventory.dir=$OUTPUT_INVENTORY_DIR")
  CMD+=("-Dinput.cli.dir=$PORTFOLIO_MANAGER_JARS")

  log_info "Running processor $PROCESSORS_DIR/util/util_portfolio-download.xml"

  log_config "input.keystore.config.file=$KEYSTORE_CONFIG_FILE
              input.cli.dir=$PORTFOLIO_MANAGER_JARS
              input.truststore.config.file=$TRUSTSTORE_CONFIG_FILE" "
              output.inventory.dir=$OUTPUT_INVENTORY_DIR"

  log_mvn "${CMD[*]}"

  if "${CMD[@]}" 2>&1 | while IFS= read -r line; do log_mvn "$line"; done; then
      log_info "Successfully ran $PROCESSORS_DIR/util/util_portfolio-download.xml"
  else
      log_error "Failed to run $PROCESSORS_DIR/util/util_portfolio-download.xml because the maven execution was unsuccessful"
      return 1
  fi
}

cleanup() {
  PID=$(lsof -ti :6466)
  if [ -z "$PID" ]; then
     echo "No process found on port $PORT"
     exit 0
  else
    kill "$PID"
    echo "Terminating running portfolio manage service"
  fi
}

main() {
  local case_file="$CASE"
  local log_level="ALL"
  local log_file="$SCRIPT_DIR/../../../../.logs/$(basename $0).log"
  local console_output_enabled=false

while getopts "c:l:f:ho" flag; do
            case "$flag" in
                c) case_file="$OPTARG" ;;
                h) print_usage; exit 0 ;;
                l) log_level="$OPTARG" ;;
                f) log_file="$OPTARG" ;;
                o) console_output_enabled=true ;;
                *) print_usage; exit 1 ;;
            esac
      done

  initialize_logger "$log_level" "$console_output_enabled" "$log_file"
  check_shared_config
  source_case_file "$case_file"
  create_required_directories
  run_portfolio_manager
  run_maven_command_portfolio_upload
  run_maven_command_portfolio_download
  cleanup
}

trap cleanup EXIT # Trap to execute cleanup even if script errors out
main "$@"
trap - EXIT # Remove the trap so subsequent scripts run correctly