#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "inventory" "enrich-inventory-from-reference"

input_file="$TEST_RESOURCES_DIR/inventories/sample-asset-001.xlsx"
output_file="$TEST_OUTPUT_DIR/enriched-inventory.xls"
reference_dir="$TEST_RESOURCES_DIR/inventories"

run_processor "$PROCESSOR_POM" \
"-Dinput.inventory.file=$input_file" \
"-Doutput.inventory.file=$output_file" \
"-Dparam.reference.inventory.dir=$reference_dir"

assert_exists "$output_file"
