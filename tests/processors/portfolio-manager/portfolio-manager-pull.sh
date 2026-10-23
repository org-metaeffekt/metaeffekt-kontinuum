#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "portfolio-manager" "portfolio-manager-pull"

require_config PORTFOLIO_MANAGER_URL
require_config PORTFOLIO_MANAGER_TOKEN
require_config PORTFOLIO_MANAGER_CLIENT_KEYSTORE_PASSWORD
require_config PORTFOLIO_MANAGER_CLIENT_TRUSTSTORE_PASSWORD

output_dir="$TEST_OUTPUT_DIR/inventories"
keystore_file="$TEST_RESOURCES_DIR/portfolio-manager/pm-client-TEST-keystore.p12"
truststore_file="$TEST_RESOURCES_DIR/portfolio-manager/pm-client-TEST-truststore.p12"

run_processor "$PROCESSOR_POM" \
"-Doutput.inventory.dir=$output_dir" \
"-Dparam.portfolio.manager.url=$PORTFOLIO_MANAGER_URL" \
"-Dparam.portfolio.manager.token=$PORTFOLIO_MANAGER_TOKEN" \
"-Dparam.project.name=metaeffekt-examples" \
"-Dparam.asset.group.id=Reports:SNAPSHOT" \
"-Dparam.asset.id=metaeffekt-examples" \
"-Dparam.keystore.password=$PORTFOLIO_MANAGER_CLIENT_KEYSTORE_PASSWORD" \
"-Dparam.truststore.password=$PORTFOLIO_MANAGER_CLIENT_TRUSTSTORE_PASSWORD" \
"-Dparam.keystore.config.file=$keystore_file" \
"-Dparam.truststore.config.file=$truststore_file" \
"-Dparam.inventory.modifier=report"

assert_nonempty_directory "$output_dir"
