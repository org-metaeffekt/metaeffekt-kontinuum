#!/bin/bash

# This pipeline is adjusted specifically to be run on remote runners with reduced runtime.

# Exit on any error
set -euo pipefail

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"
CASES_DIR="$SELF_DIR/../cases"
LOG_FILE="$SELF_DIR/../../../.logs/$(basename $0).log"

# Maintain script execution order or scripts might break

bash "$PROCESSOR_SCRIPTS_DIR/mirror/mirror_download-index.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/prepare/prepare_save-inspect-image.sh" -c "$CASES_DIR/prepare/prepare_save-inspect-image-02.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/prepare/prepare_copy-pom-dependencies.sh" -c "$CASES_DIR/prepare/prepare_copy-pom-dependencies-02.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/extract/extract_scan-directory.sh" -c "$CASES_DIR/extract/extract_scan-directory-02.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/extract/extract_scan-directory.sh" -c "$CASES_DIR/extract/extract_scan-directory-03.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/advise/advise_attach-metadata.sh" -c "$CASES_DIR/advise/advise_attach-metadata-03.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/advise/advise_enrich-with-reference.sh" -c "$CASES_DIR/advise/advise_enrich-with-reference-02.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/advise/advise_enrich-inventory.sh" -c "$CASES_DIR/advise/advise_enrich-inventory-02.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/advise/advise_create-dashboard.sh" -c "$CASES_DIR/advise/advise_create-dashboard-02.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/convert/convert_inventory-to-cyclonedx.sh" -c "$CASES_DIR/convert/convert_inventory-to-cyclonedx-02.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/convert/convert_cyclonedx-to-inventory.sh" -c "$CASES_DIR/convert/convert_cyclonedx-to-inventory-02.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/convert/convert_inventory-to-spdx.sh" -c "$CASES_DIR/convert/convert_inventory-to-spdx-02.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/report/report_create-document.sh" -c "$CASES_DIR/report/report_create-document-02.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/portfolio/portfolio_copy-resources.sh" -c "$CASES_DIR/portfolio/portfolio_copy-resources-02.sh" -f "$LOG_FILE"
