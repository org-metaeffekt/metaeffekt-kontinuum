#!/bin/bash

# Exit on any error
set -euo pipefail

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"
CASES_DIR="$SELF_DIR/../cases"
LOG_FILE="$SELF_DIR/../../../.logs/$(basename $0).log"

# Maintain script execution order or scripts might break

bash "$PROCESSOR_SCRIPTS_DIR/mirror/mirror_download-index.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/util/util_create-diff.sh" -c "$CASES_DIR/util/util_create-diff-02.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/util/util_merge-inventories.sh" -c "$CASES_DIR/util/util_merge-inventories-02.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/util/util_transform-inventories.sh" -c "$CASES_DIR/util/util_transform-inventories-02.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/util/util_validate-reference-inventory.sh" -c "$CASES_DIR/util/util_validate-reference-inventory-02.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/advise/advise_attach-metadata.sh" -c "$CASES_DIR/advise/advise_attach-metadata-02.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/advise/advise_enrich-with-reference.sh" -c "$CASES_DIR/advise/advise_enrich-with-reference-02.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/advise/advise_enrich-inventory.sh" -c "$CASES_DIR/advise/advise_enrich-inventory-02.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/advise/advise_create-dashboard.sh" -c "$CASES_DIR/advise/advise_create-dashboard-02.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/convert/convert_inventory-to-cyclonedx.sh" -c "$CASES_DIR/convert/convert_inventory-to-cyclonedx-02.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/convert/convert_cyclonedx-to-inventory.sh" -c "$CASES_DIR/convert/convert_cyclonedx-to-inventory-02.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/convert/convert_inventory-to-spdx.sh" -c "$CASES_DIR/convert/convert_inventory-to-spdx-02.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/report/report_create-document.sh" -c "$CASES_DIR/report/report_create-document-02.sh" -f "$LOG_FILE"

