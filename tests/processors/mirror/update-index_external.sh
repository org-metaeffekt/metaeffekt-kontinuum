#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "mirror" "update-index_external"

require_config VULNERABILITY_MIRROR_DIR
require_directory "$VULNERABILITY_MIRROR_DIR"

mirror_dir="$TEST_OUTPUT_DIR/mirror"
mkdir -p "$mirror_dir"
cp -a "$VULNERABILITY_MIRROR_DIR/." "$mirror_dir/"

run_processor "$PROCESSOR_POM" \
"-Denv.mirror.dir=$mirror_dir"

assert_nonempty_directory "$mirror_dir/.database"
