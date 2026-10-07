#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "inventory" "transform-inventories"

input_dir="$TEST_RESOURCES_DIR/inventories"
output_file="$TEST_OUTPUT_DIR/transformed-inventory.xls"
script_file="$TEST_RESOURCES_DIR/misc/transform-inventories.kts"

run_processor "$PROCESSOR_POM" \
"-Dinput.inventory.dir=$input_dir" \
"-Doutput.inventory.dir=$output_file" \
"-Dparam.kotlin.script.file=$script_file"

assert_exists "$output_file"
