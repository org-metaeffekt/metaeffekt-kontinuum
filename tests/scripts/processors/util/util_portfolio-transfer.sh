#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="util/util_portfolio-transfer-01.sh"

# Check if config.sh exists and source it
if [[ -f "$CONFIG_PATH" ]]; then
    source "$CONFIG_PATH"
else
    echo "Error: config.sh not found at $CONFIG_PATH" >&2
    exit 1
fi


if [ -d "$RESOURCES_DIR/../scripts/target/util/portfolio-manager" ]; then
  rm -r "$RESOURCES_DIR/../scripts/target/util/portfolio-manager"
fi

# Setup for portfolio manager
cd "$RESOURCES_DIR/portfolio-manager/service"
java -jar "$UTIL_DIR/portfolio-manager-jars/ae-portfolio-manager-service-0.5.0.jar" &

cd "$RESOURCES_DIR/portfolio-manager/client"
OUTPUT=$(java -jar "$UTIL_DIR/portfolio-manager-jars/ae-portfolio-manager-cli-0.5.0-exec.jar" create-project A)
JSON_OUTPUT=$(echo "$OUTPUT" | sed -n '/^{/,$p')
ADMIN_TOKEN=$(echo "$JSON_OUTPUT" | jq -r '.body.adminToken')

cd "$SCRIPT_DIR"

# Run maven commands
CMD=(mvn -f "$PROCESSORS_DIR/util/util_portfolio-upload.xml" process-resources)
CMD+=("-Dparam.portfolio.manager.url=$PORTFOLIO_MANAGER_URL")
CMD+=("-Dparam.portfolio.manager.token=$ADMIN_TOKEN")
CMD+=("-Dinput.keystore.config.file=$KEYSTORE_CONFIG_FILE")
CMD+=("-Dparam.keystore.password=$KEYSTORE_PASSWORD")
CMD+=("-Dinput.truststore.config.file=$TRUSTSTORE_CONFIG_FILE")
CMD+=("-Dparam.truststore.password=$TRUSTSTORE_PASSWORD")
CMD+=("-Dparam.product.name=$PRODUCT_NAME")
CMD+=("-Dparam.product.version=$PRODUCT_VERSION")
CMD+=("-Dparam.product.artifact.id=$PRODUCT_ARTIFACT_ID")
CMD+=("-Dinput.file=$INPUT_FILE")
CMD+=("-Dinput.cli.dir=$UTIL_DIR/portfolio-manager-jars")


echo "${CMD[@]}"
"${CMD[@]}"

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
CMD+=("-Dinput.cli.dir=$UTIL_DIR/portfolio-manager-jars")

echo "${CMD[@]}"
"${CMD[@]}"

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

trap cleanup EXIT