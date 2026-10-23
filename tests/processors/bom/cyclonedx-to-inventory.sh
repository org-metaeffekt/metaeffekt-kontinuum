#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "bom" "cyclonedx-to-inventory"

input_file="$TEST_RESOURCES_DIR/bom/sample-cyclonedx.json"
output_file="$TEST_OUTPUT_DIR/inventory.xls"

run_processor "$PROCESSOR_POM" \
"-Dinput.bom.file=$input_file" \
"-Doutput.inventory.file=$output_file"

assert_exists "$output_file"
