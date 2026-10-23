#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "mirror" "download-data-sources"

require_config NVD_API_KEY

mirror_dir="$TEST_OUTPUT_DIR/mirror"

run_processor "$PROCESSOR_POM" \
"-Denv.mirror.dir=$mirror_dir" \
"-Denv.nvd.apikey=$NVD_API_KEY"

assert_nonempty_directory "$mirror_dir"
