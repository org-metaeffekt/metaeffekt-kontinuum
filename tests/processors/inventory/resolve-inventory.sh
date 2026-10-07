#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "inventory" "resolve-inventory"

input_file="$TEST_RESOURCES_DIR/inventories/sample-asset-001.xlsx"
output_file="$TEST_OUTPUT_DIR/resolved-inventory.xls"
config_file="$TEST_RESOURCES_DIR/resolver/sample-artifact-resolver-config.yaml"
proxy_file="$TEST_RESOURCES_DIR/resolver/sample-artifact-resolver-proxy.yaml"

run_processor "$PROCESSOR_POM" \
"-Dinput.inventory.file=$input_file" \
"-Doutput.inventory.file=$output_file" \
"-Denv.maven.index.dir=$TEST_OUTPUT_DIR/maven-index" \
"-Dparam.artifact.resolver.config.file=$config_file" \
"-Dparam.artifact.resolver.proxy.file=$proxy_file"

assert_exists "$output_file"
