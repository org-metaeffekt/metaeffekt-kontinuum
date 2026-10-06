#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "report" "copy-overview-resources"

inventories_dir="$TEST_RESOURCES_DIR/inventories"
dashboards_dir="$TEST_RESOURCES_DIR/dashboard"
reports_dir="$TEST_RESOURCES_DIR/report"
advisor_inventories_dir="$TEST_RESOURCES_DIR/inventories"
output_dir="$TEST_OUTPUT_DIR/overview-resources"

run_processor "$PROCESSOR_POM" \
"-Dinput.inventories.dir=$inventories_dir" \
"-Dinput.dashboards.dir=$dashboards_dir" \
"-Dinput.reports.dir=$reports_dir" \
"-Dinput.advisor.inventories.dir=$advisor_inventories_dir" \
"-Doutput.resources.dir=$output_dir"

assert_nonempty_directory "$output_dir"
