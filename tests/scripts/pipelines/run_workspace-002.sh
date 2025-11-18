#!/bin/bash

# Exit on any error
set -euo pipefail

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"
CASES_DIR="$SELF_DIR/../cases"
LOG_FILE="$SELF_DIR/../../../.logs/$(basename $0).log"

bash "$PROCESSOR_SCRIPTS_DIR/mirror/mirror_download-index.sh" -f "$LOG_FILE"

 # FIXME: check availability before executing
bash "$PROCESSOR_SCRIPTS_DIR/prepare/prepare_save-inspect-image.sh" -c "$CASES_DIR/prepare/prepare_save-inspect-image-02.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/prepare/prepare_copy-pom-dependencies.sh" -c "$CASES_DIR/prepare/prepare_copy-pom-dependencies-02.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/extract/extract_scan-directory.sh" -c "$CASES_DIR/extract/extract_scan-directory-02.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/extract/extract_scan-directory.sh" -c "$CASES_DIR/extract/extract_scan-directory-03.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/analyze/analyze_resolve-inventory.sh" -c "$CASES_DIR/analyze/analyze_resolve-inventory-02.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/advise/advise_attach-metadata.sh" -c "$CASES_DIR/advise/advise_attach-metadata-02.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/advise/advise_enrich-with-reference.sh" -c "$CASES_DIR/advise/advise_enrich-with-reference-02.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/advise/advise_enrich-inventory.sh" -c "$CASES_DIR/advise/advise_enrich-inventory-02.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/advise/advise_create-dashboard.sh" -c "$CASES_DIR/advise/advise_create-dashboard-02.sh" -f "$LOG_FILE"

# Disabled due to a dependency on a running scancode service
# bash "$PROCESSOR_SCRIPTS_DIR/scan/scan_scan-inventory.sh" -c "$CASES_DIR/scan/scan_scan-inventory-02.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/report/report_create-document.sh" -c "$CASES_DIR/report/report_create-document-02.sh" -f "$LOG_FILE"
