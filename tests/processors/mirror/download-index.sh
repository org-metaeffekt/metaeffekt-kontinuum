#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "mirror" "download-index"

require_config VULNERABILITY_MIRROR_URL

mirror_dir="$TEST_OUTPUT_DIR/mirror"

run_processor "$PROCESSOR_POM" \
"-Denv.vulnerability.mirror.dir=$mirror_dir" \
"-Dparam.mirror.archive.url=$VULNERABILITY_MIRROR_URL"

assert_nonempty_directory "$mirror_dir/.database"
