#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../shared.sh"

setup_processor_test "report" "create-annex-archive"

pdf_file="$TEST_RESOURCES_DIR/report/sample-report.pdf"
archive_file="$TEST_OUTPUT_DIR/annex.zip"

run_processor "$PROCESSOR_POM" \
"-Dinput.document.en.pdf.file=$pdf_file" \
"-Doutput.annex.archive.file=$archive_file"

assert_exists "$archive_file"
