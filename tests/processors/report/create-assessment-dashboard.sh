#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "report" "create-assessment-dashboard"

require_config VULNERABILITY_MIRROR_DIR
require_directory "$VULNERABILITY_MIRROR_DIR"

input_file="$TEST_RESOURCES_DIR/inventories/sample-asset-001.xlsx"
output_file="$TEST_OUTPUT_DIR/dashboard.html"
policy_file="$TEST_RESOURCES_DIR/dashboard/sample-security-policy.json"

run_processor "$PROCESSOR_POM" \
"-Dinput.inventory.file=$input_file" \
"-Doutput.dashboard.file=$output_file" \
"-Dparam.security.policy.file=$policy_file" \
"-Dparam.tenant.id=metaeffekt" \
"-Dparam.asset.id=sample-product-1" \
"-Dparam.assessment.context=local" \
"-Denv.vulnerability.mirror.dir=$VULNERABILITY_MIRROR_DIR" \
"-Denv.vulnerability.assessment.api=NONE"

assert_exists "$output_file"
