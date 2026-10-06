#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "assessment" "convert-assessments"

assessment_dir="$TEST_RESOURCES_DIR/assessments/"
output_dir="$TEST_OUTPUT_DIR"

run_processor "$PROCESSOR_POM" \
"-Dinput.assessment.dir=$assessment_dir" \
"-Doutput.assessment.dir=$output_dir" \
"-Dparam.output.mode=DIRECTORY" \
"-Dparam.output.format=YAML"

assert_nonempty_directory "$output_dir"
