#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "portfolio-manager" "portfolio-manager-push"

require_config PORTFOLIO_MANAGER_URL
require_config PORTFOLIO_MANAGER_TOKEN
require_config PORTFOLIO_MANAGER_CLIENT_KEYSTORE_PASSWORD
require_config PORTFOLIO_MANAGER_CLIENT_TRUSTSTORE_PASSWORD

input_file="$TEST_RESOURCES_DIR/inventories/sample-asset-001.xlsx"
keystore_file="$TEST_RESOURCES_DIR/portfolio-manager/sample-pm-client-keystore.p12"
truststore_file="$TEST_RESOURCES_DIR/portfolio-manager/sample-pm-client-truststore.p12"

run_processor "$PROCESSOR_POM" \
"-Dinput.file=$input_file" \
"-Dparam.portfolio.manager.url=$PORTFOLIO_MANAGER_URL" \
"-Dparam.portfolio.manager.token=$PORTFOLIO_MANAGER_TOKEN" \
"-Dparam.project.name=metaeffekt" \
"-Dparam.asset.group.id=sample-products" \
"-Dparam.asset.name=sample-product-1" \
"-Dparam.asset.version=1.0.0" \
"-Dparam.keystore.config.file=$keystore_file" \
"-Dparam.truststore.config.file=$truststore_file" \
"-Dparam.keystore.password=$PORTFOLIO_MANAGER_CLIENT_KEYSTORE_PASSWORD" \
"-Dparam.truststore.password=$PORTFOLIO_MANAGER_CLIENT_TRUSTSTORE_PASSWORD"
