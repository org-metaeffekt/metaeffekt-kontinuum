#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PRELOAD_SCRIPT_PATH="$SCRIPT_DIR/../preload.sh"
SHARED_SCRIPT_PATH="$SCRIPT_DIR/../shared.sh"
LOGGER_PATH="$SCRIPT_DIR/../log.sh"
CASE="util/util_portfolio-transfer-01.sh"


source_shared() {
  if [[ -f "$SHARED_SCRIPT_PATH" ]]; then
      source "$SHARED_SCRIPT_PATH"
  else
      log_error "Config file not found at $SHARED_SCRIPT_PATH"
      exit 1
  fi
}

source_preload() {
  if [ -z "${PRELOAD_LOADED:-}" ]; then
    PRELOAD_LOADED="true"
    export PRELOAD_LOADED

    if [[ -f "$PRELOAD_SCRIPT_PATH" ]]; then
        source "$PRELOAD_SCRIPT_PATH"
    else
        log_error "Config file not found at $PRELOAD_SCRIPT_PATH"
        exit 1
    fi
  fi
}

initialize_logger() {
    local log_file="$1"
    source $LOGGER_PATH
    logger_init "$log_file"
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
  [ -n "${AE_CORE_VERSION:-}" ] && CMD+=("-Dae.core.version=$AE_CORE_VERSION")
  [ -n "${AE_ARTIFACT_ANALYSIS_VERSION:-}" ] && CMD+=("-Dae.artifact.analysis.version=$AE_ARTIFACT_ANALYSIS_VERSION")
  CMD+=("-Dinput.file=$INPUT_FILE")
  CMD+=("-Dinput.cli.dir=$PORTFOLIO_MANAGER_JARS")
  CMD+=("-Dparam.portfolio.manager.url=$PORTFOLIO_MANAGER_URL")
  CMD+=("-Dparam.portfolio.manager.token=$ADMIN_TOKEN")
  CMD+=("-Dparam.product.name=$PRODUCT_NAME")
  CMD+=("-Dparam.product.version=$PRODUCT_VERSION")
  CMD+=("-Dparam.product.artifact.id=$PRODUCT_ARTIFACT_ID")
  CMD+=("-Denv.truststore.config.file=$TRUSTSTORE_CONFIG_FILE")
  CMD+=("-Denv.truststore.password=$TRUSTSTORE_PASSWORD")
  CMD+=("-Denv.keystore.config.file=$KEYSTORE_CONFIG_FILE")
  CMD+=("-Denv.keystore.password=$KEYSTORE_PASSWORD")

  pass_command_info_to_logger "$(basename "$0")"
}

run_maven_command_portfolio_download() {
  cd "$SCRIPT_DIR"

  CMD=(mvn -f "$PROCESSORS_DIR/util/util_portfolio-download.xml" process-resources)
  [ -n "${AE_CORE_VERSION:-}" ] && CMD+=("-Dae.core.version=$AE_CORE_VERSION")
  [ -n "${AE_ARTIFACT_ANALYSIS_VERSION:-}" ] && CMD+=("-Dae.artifact.analysis.version=$AE_ARTIFACT_ANALYSIS_VERSION")
  CMD+=("-Dparam.portfolio.manager.url=$PORTFOLIO_MANAGER_URL")
  CMD+=("-Dparam.portfolio.manager.token=$ADMIN_TOKEN")
  CMD+=("-Dparam.product.name=$PRODUCT_NAME")
  CMD+=("-Dparam.product.version=$PRODUCT_VERSION")
  CMD+=("-Dparam.product.artifact.id=$PRODUCT_ARTIFACT_ID")
  CMD+=("-Doutput.inventory.dir=$OUTPUT_INVENTORY_DIR")
  CMD+=("-Dinput.cli.dir=$PORTFOLIO_MANAGER_JARS")
  CMD+=("-Denv.truststore.config.file=$TRUSTSTORE_CONFIG_FILE")
  CMD+=("-Denv.truststore.password=$TRUSTSTORE_PASSWORD")
  CMD+=("-Denv.keystore.config.file=$KEYSTORE_CONFIG_FILE")
  CMD+=("-Denv.keystore.password=$KEYSTORE_PASSWORD")

  pass_command_info_to_logger "$(basename "$0")"
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

  local log_file="$SCRIPT_DIR/../../../../.logs/$(basename $0).log"


while getopts "c:l:f:ho" flag; do
            case "$flag" in
                c) case_file="$OPTARG" ;;
                h) print_usage; exit 0 ;;
                l) log_level="$OPTARG" ;;
                f) log_file="$OPTARG" ;;
                o) console_output_enabled=false ;;
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