#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "inventory" "copy-inventories"

input_dir="$TEST_RESOURCES_DIR/inventories/samples"
output_dir="$TEST_OUTPUT_DIR/inventories-out"

run_processor "$PROCESSOR_POM" \
"-Dinput.base.dir=$input_dir" \
"-Doutput.inventories.dir=$output_dir" \
"-Dparam.inventories.list=sample-inventory-small.xlsx"

assert_exists "$output_dir/sample-inventory-small.xlsx"
