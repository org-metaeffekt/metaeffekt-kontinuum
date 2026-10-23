#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "artifacts" "download-maven-artifact"

param_group_id="org.apache.commons"
param_artifact_id="commons-lang3"
param_version="3.14.0"

run_processor "$PROCESSOR_POM" \
"-Doutput.asset.dir=$TEST_OUTPUT_DIR" \
"-Dparam.group.id=$param_group_id" \
"-Dparam.artifact.id=$param_artifact_id" \
"-Dparam.version=$param_version"

assert_exists "$TEST_OUTPUT_DIR/commons-lang3-3.14.0.jar"
