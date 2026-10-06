#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "report" "aggregate-source-artifacts"

input_file="$TEST_RESOURCES_DIR/inventories/sample-asset-001.xlsx"
config_file="$TEST_RESOURCES_DIR/misc/sample-source-aggregation.yaml"
output_dir="$TEST_OUTPUT_DIR/sources"

run_processor "$PROCESSOR_POM" \
"-Dinput.inventory.file=$input_file" \
"-Doutput.target.dir=$output_dir" \
"-Dparam.config.file=$config_file" \
"-Dparam.fail.on.missing.sources=true"

assert_nonempty_directory "$output_dir"
