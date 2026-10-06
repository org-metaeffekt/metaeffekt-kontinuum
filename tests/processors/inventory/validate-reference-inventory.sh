#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "inventory" "validate-reference-inventory"

input_dir="$TEST_RESOURCES_DIR/inventories/reference-inventory"

run_processor "$PROCESSOR_POM" \
"-Dinput.inventory.dir=$input_dir"

assert_exists "$input_dir/inventory-update/artifact-inventory.xls"
