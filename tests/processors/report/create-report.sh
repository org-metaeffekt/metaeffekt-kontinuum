#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "report" "create-report"

require_config AE_WORKBENCH_DIR
require_directory "$AE_WORKBENCH_DIR"
require_config VULNERABILITY_MIRROR_DIR
require_directory "$VULNERABILITY_MIRROR_DIR"

input_dir="$TEST_RESOURCES_DIR/inventories"
reference_dir="$TEST_RESOURCES_DIR/inventories"
output_file="$TEST_OUTPUT_DIR/report.pdf"
computed_dir="$TEST_OUTPUT_DIR/computed-inventory"
descriptor_file="$TEST_RESOURCES_DIR/report/asset-descriptor_GENERIC-vulnerability-report.yaml"
policy_file="$TEST_RESOURCES_DIR/dashboard/sample-security-policy.json"

run_processor "$PROCESSOR_POM" \
"-Dinput.inventory.dir=$input_dir" \
"-Doutput.document.file=$output_file" \
"-Dparam.document.type=VR" \
"-Dparam.asset.id=sample-product-1" \
"-Dparam.asset.name=sample-product" \
"-Dparam.asset.version=1.0.0" \
"-Dparam.product.name=sample-product" \
"-Dparam.product.version=1.0.0" \
"-Dparam.product.watermark=TEST" \
"-Dparam.property.selector.organization=metaeffekt" \
"-Dparam.asset.descriptor.file=$descriptor_file" \
"-Dparam.security.policy.file=$policy_file" \
"-Dparam.reference.inventory.dir=$reference_dir" \
"-Dparam.overview.advisors=CERT_FR" \
"-Dparam.computed.inventory.dir=$computed_dir" \
"-Denv.kontinuum.dir=$KONTINUUM_DIR" \
"-Denv.workbench.dir=$AE_WORKBENCH_DIR" \
"-Denv.vulnerability.mirror.dir=$VULNERABILITY_MIRROR_DIR"

assert_exists "$output_file"
assert_exists "$computed_dir"
