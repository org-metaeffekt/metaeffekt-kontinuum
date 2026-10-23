#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "licensing" "aggregate-licenses"

require_config TMD_PASSWORD

userkeys_file="$TEST_RESOURCES_DIR/misc/kosmos.consumer.keys"
input_file="$TEST_RESOURCES_DIR/inventories/samples/sample-inventory-small.xlsx"
reference_dir="$TEST_RESOURCES_DIR/inventories"

run_processor "$PROCESSOR_POM" \
"-Dinput.inventory.file=$input_file" \
"-Dparam.reference.inventory.dir=$reference_dir" \
"-Dparam.reference.components.dir=$reference_dir" \
"-Dparam.reference.licenses.dir=$reference_dir" \
"-Dparam.target.components.dir=$TEST_OUTPUT_DIR/components" \
"-Dparam.target.licenses.dir=$TEST_OUTPUT_DIR/licenses" \
"-Denv.tmd.source=${TMD_SOURCE:-kosmos}" \
"-Denv.tmd.userkeys.file=$userkeys_file" \
"-Denv.tmd.password=$TMD_PASSWORD"
