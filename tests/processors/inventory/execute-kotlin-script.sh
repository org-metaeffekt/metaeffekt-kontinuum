#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "inventory" "execute-kotlin-script"

script_file="$TEST_RESOURCES_DIR/misc/filter-inventory.kts"
input_file="$TEST_RESOURCES_DIR/inventories/sample-asset-001.xlsx"
output_file="$TEST_OUTPUT_DIR/filtered-inventory.xls"

run_processor "$PROCESSOR_POM" \
"-Dinput.kotlin.script.file=$script_file" \
"-Dinput.inventory.file=$input_file" \
"-Doutput.inventory.file=$output_file" \
"-Dparam.asset.id=sample-product-1"

assert_exists "$output_file"
