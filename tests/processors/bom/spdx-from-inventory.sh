#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "bom" "spdx-from-inventory"

input_file="$TEST_RESOURCES_DIR/inventories/sample-asset-001.xlsx"
output_file="$TEST_OUTPUT_DIR/spdx.json"

run_processor "$PROCESSOR_POM" \
"-Dinput.inventory.file=$input_file" \
"-Doutput.bom.file=$output_file" \
"-Dparam.document.name=sample-product" \
"-Dparam.document.organization=metaeffekt" \
"-Dparam.document.organization.url=https://metaeffekt.com" \
"-Dparam.document.output.format=JSON"

assert_exists "$output_file"
