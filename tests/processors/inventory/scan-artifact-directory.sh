#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "inventory" "scan-artifact-directory"

input_dir="$TEST_RESOURCES_DIR/misc"
reference_dir="$TEST_RESOURCES_DIR/inventories"
scan_dir="$TEST_OUTPUT_DIR/scan-output"
output_file="$TEST_OUTPUT_DIR/scanned-inventory.xls"

run_processor "$PROCESSOR_POM" \
"-Dinput.extract.dir=$input_dir" \
"-Doutput.scan.dir=$scan_dir" \
"-Doutput.inventory.file=$output_file" \
"-Dparam.reference.inventory.dir=$reference_dir"

assert_nonempty_directory "$scan_dir"
assert_exists "$output_file"
