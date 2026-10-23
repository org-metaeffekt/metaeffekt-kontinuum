#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "portfolio-manager" "download-portfolio-manager-jars"

cli_dir="$TEST_OUTPUT_DIR/cli"

run_processor "$PROCESSOR_POM" \
"-Dinput.cli.dir=$cli_dir"

assert_nonempty_directory "$cli_dir"
