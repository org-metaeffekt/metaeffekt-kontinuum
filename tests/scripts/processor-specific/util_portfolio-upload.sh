#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SCRIPT_DIR/../config.sh"
CASE="util_portfolio-upload-01.sh"

# Check if config.sh exists and source it
if [[ -f "$CONFIG_PATH" ]]; then
    source "$CONFIG_PATH"
else
    echo "Error: config.sh not found at $CONFIG_PATH" >&2
    exit 1
fi

# Parse command line options
while getopts "c:" flag; do
    case "$flag" in
        c) CASE="$OPTARG" ;;
        *) cat <<EOF
            Usage: $0 [options]
                -c <case>   : which case to select for running the test. Either an absolute path or relative to the CASES_DIR, defined in the config.sh
                -h          : show this help message

            EXAMPLE:
                ./script.sh -c 1
EOF
    esac
done

# Check if selected case exists
if [ -f "$CASES_DIR/$CASE" ]; then
   source "$CASES_DIR/$CASE"
elif [ -f "$CASE" ]; then
   source "$CASE"
else
    echo "Error: Case [$CASE] does not exist. Has to be either relative to [$CASES_DIR] or as an absolute path."
    exit 1
fi

echo "$PROCESSORS_DIR"

if [ -d "$RESOURCES_DIR/../scripts/target/util/portfolio-manager" ]; then
  rm -r "$RESOURCES_DIR/../scripts/target/util/portfolio-manager"
fi

# Setup for
cd "$RESOURCES_DIR/portfolio-manager/service"
java -jar "$RESOURCES_DIR/portfolio-manager/service/ae-portfolio-manager-service.jar" &
SERVICE_PID=$!

cd "$RESOURCES_DIR/portfolio-manager/client"
OUTPUT=$(java -jar "$RESOURCES_DIR/portfolio-manager/client/ae-portfolio-manager-cli.jar" create-project A)
JSON_OUTPUT=$(echo "$OUTPUT" | sed -n '/^{/,$p')
ADMIN_TOKEN=$(echo "$JSON_OUTPUT" | jq -r '.body.adminToken')

cd "$SCRIPT_DIR"

# Run maven command
CMD=(mvn -f "$PROCESSORS_DIR/util_portfolio-upload.xml" process-resources)
CMD+=("-Dportfolio.manager.url=$PORTFOLIO_MANAGER_URL")
CMD+=("-Dportfolio.manager.token=$ADMIN_TOKEN")
CMD+=("-Dkeystore.config.file=$KEYSTORE_CONFIG_FILE")
CMD+=("-Dkeystore.password=$KEYSTORE_PASSWORD")
CMD+=("-Dtruststore.config.file=$TRUSTSTORE_CONFIG_FILE")
CMD+=("-Dtruststore.password=$TRUSTSTORE_PASSWORD")
CMD+=("-Dproduct.name=$PRODUCT_NAME")
CMD+=("-Dproduct.version=$PRODUCT_VERSION")
CMD+=("-Dproduct.artifact.id=$PRODUCT_ARTIFACT_ID")
CMD+=("-Dinput.file=$INPUT_FILE")

echo "${CMD[@]}"
"${CMD[@]}"

if [ -n "$SERVICE_PID" ]; then
    echo "Terminating service process (PID: $SERVICE_PID)..."
    kill $SERVICE_PID
    wait $SERVICE_PID 2>/dev/null
    echo "Service terminated."
else
    echo "Warning: No service PID was captured; skipping termination."
fi