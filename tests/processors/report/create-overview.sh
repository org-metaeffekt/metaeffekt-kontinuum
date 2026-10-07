#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "report" "create-overview"

base_dir="$TEST_RESOURCES_DIR"
input_path="inventories"
advisor_inventories_path="inventories"
dashboards_path="dashboard"
reports_path="report"
output_file="$TEST_OUTPUT_DIR/overview.html"
policy_file="$TEST_RESOURCES_DIR/dashboard/sample-security-policy.json"

run_processor "$PROCESSOR_POM" \
"-Dinput.inventory.dir=$base_dir" \
"-Dinput.inventory.path=$input_path" \
"-Dinput.advisor.inventories.dir=$advisor_inventories_path" \
"-Dinput.dashboards.dir=$dashboards_path" \
"-Dinput.reports.dir=$reports_path" \
"-Doutput.overview.file=$output_file" \
"-Dparam.security.policy.file=$policy_file"

assert_exists "$output_file"
