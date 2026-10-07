#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "artifacts" "download-asset"

asset_url="https://repo.maven.apache.org/maven2/org/apache/commons/commons-lang3/3.14.0/commons-lang3-3.14.0.pom"

run_processor "$PROCESSOR_POM" \
 "-Doutput.asset.dir=$TEST_OUTPUT_DIR" \
 "-Dparam.asset.url=$asset_url"

assert_nonempty_directory "$TEST_OUTPUT_DIR"
