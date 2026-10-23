#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "licensing" "apply-business-case"

require_config TMD_PASSWORD
require_config TMD_SOURCE


input_file="$TEST_RESOURCES_DIR/inventories/samples/sample-inventory-small.xlsx"
output_file="$TEST_OUTPUT_DIR/business-case-inventory.xls"
reference_dir="$TEST_RESOURCES_DIR/inventories"
userkeys_file="$TEST_RESOURCES_DIR/misc/kosmos.consumer.keys"

run_processor "$PROCESSOR_POM" \
"-Dinput.inventory.file=$input_file" \
"-Doutput.inventory.file=$output_file" \
"-Dparam.reference.inventory.dir=$reference_dir" \
"-Denv.tmd.userkeys.file=$userkeys_file" \
"-Denv.tmd.source=$TMD_SOURCE" \
"-Denv.tmd.password=$TMD_PASSWORD"

assert_exists "$output_file"
