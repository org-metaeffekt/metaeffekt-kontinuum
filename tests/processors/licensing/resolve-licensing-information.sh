#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "licensing" "resolve-licensing-information"

require_config TMD_PASSWORD

userkeys_file="$TEST_RESOURCES_DIR/misc/kosmos.consumer.keys"
input_file="$TEST_RESOURCES_DIR/inventories/samples/sample-inventory-small.xlsx"
output_file="$TEST_OUTPUT_DIR/licensed-inventory.xls"
# TODO: tests/resources/misc/sample-properties.yaml is required by this test but is not committed yet.
properties_file="$TEST_RESOURCES_DIR/misc/sample-properties.yaml"

run_processor "$PROCESSOR_POM" \
"-Dinput.inventory.file=$input_file" \
"-Doutput.inventory.file=$output_file" \
"-Dinput.output.analysis.base.dir=$TEST_OUTPUT_DIR/analysis" \
"-Dparam.properties.file=$properties_file" \
"-Denv.kosmos.password=$TMD_PASSWORD" \
"-Denv.kosmos.userkeys.file=$userkeys_file"

assert_exists "$output_file"
