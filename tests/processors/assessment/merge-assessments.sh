#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "assessment" "merge-assessments"

input_dir="$TEST_RESOURCES_DIR/inventories"
output_file="$TEST_OUTPUT_DIR/merged-inventory.xls"

run_processor "$PROCESSOR_POM" \
"-Dinput.inventory.dir=$input_dir" \
"-Doutput.inventory.file=$output_file"

assert_exists "$output_file"
