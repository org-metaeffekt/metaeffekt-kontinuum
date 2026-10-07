#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "licensing" "aggregate-reference-licenses"

input_file="$TEST_RESOURCES_DIR/inventories/sample-asset-001.xlsx"
reference_dir="$TEST_RESOURCES_DIR/inventories"

run_processor "$PROCESSOR_POM" \
"-Dinput.inventory.file=$input_file" \
"-Dparam.reference.inventory.dir=$reference_dir" \
"-Dparam.reference.components.dir=$reference_dir" \
"-Dparam.reference.licenses.dir=$reference_dir" \
"-Dparam.target.components.dir=$TEST_OUTPUT_DIR/components" \
"-Dparam.target.licenses.dir=$TEST_OUTPUT_DIR/licenses"

assert_exists "$TEST_OUTPUT_DIR/components"
assert_exists "$TEST_OUTPUT_DIR/licenses"
