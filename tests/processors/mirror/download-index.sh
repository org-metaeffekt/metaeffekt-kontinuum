#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "mirror" "download-index"

require_config VULNERABILITY_MIRROR_URL
require_config VULNERABILITY_MIRROR_DIR

run_processor "$PROCESSOR_POM" \
"-Denv.vulnerability.mirror.dir=$VULNERABILITY_MIRROR_DIR" \
"-Dparam.mirror.archive.url=$VULNERABILITY_MIRROR_URL"

assert_nonempty_directory "$VULNERABILITY_MIRROR_DIR/.database"
