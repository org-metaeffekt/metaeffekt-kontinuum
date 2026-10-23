#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "inventory" "transform-inventories"

input_dir="$TEST_RESOURCES_DIR/inventories/samples"
output_dir="$TEST_OUTPUT_DIR"
script_file="$TEST_RESOURCES_DIR/misc/transform-inventories.kts"

run_processor "$PROCESSOR_POM" \
"-Dinput.inventory.dir=$input_dir" \
"-Doutput.inventory.dir=$output_dir" \
"-Dparam.kotlin.script.file=$script_file"