#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "artifacts" "save-container-image"

param_image_id="nginx"
param_image_version="1.21.6"

run_processor "$PROCESSOR_POM" \
"-Doutput.dir=$TEST_OUTPUT_DIR" \
"-Dparam.image.id=$param_image_id" \
"-Dparam.image.version=$param_image_version"

assert_exists "$TEST_OUTPUT_DIR/nginx-1.21.6.json"
assert_exists "$TEST_OUTPUT_DIR/nginx-1.21.6.tar"
