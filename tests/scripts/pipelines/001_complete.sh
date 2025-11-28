#!/bin/bash

# Exit on any error
set -euo pipefail

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"
CASES_DIR="$SELF_DIR/../cases"
LOG_FILE="$SELF_DIR/../../../.logs/$(basename $0).log"

bash "$PROCESSOR_SCRIPTS_DIR/mirror/mirror_download-index.sh" -c "$CASES_DIR/mirror/mirror_download-index-01.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/util/util_aggregate-sources.sh" -c "$CASES_DIR/util/util_aggregate-sources-01.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/util/util_copy-inventories.sh" -c "$CASES_DIR/util/util_copy-inventories-01.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/util/util_create-diff.sh" -c "$CASES_DIR/util/util_create-diff-01.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/util/util_merge-inventories.sh" -c "$CASES_DIR/util/util_merge-inventories-01.sh" -f "$LOG_FILE"

#Disabled due to fail on first run
#bash "$PROCESSOR_SCRIPTS_DIR/util/util_portfolio-transfer.sh" -c "$CASES_DIR/util/util_portfolio-transfer-01.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/util/util_transform-inventories.sh" -c "$CASES_DIR/util/util_transform-inventories-01.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/util/util_validate-reference-inventory.sh" -c "$CASES_DIR/util/util_validate-reference-inventory-01.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/prepare/prepare_copy-pom-dependencies.sh" -c "$CASES_DIR/prepare/prepare_copy-pom-dependencies-01.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/prepare/prepare_save-inspect-image.sh" -c "$CASES_DIR/prepare/prepare_save-inspect-image-01.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/extract/extract_scan-directory.sh" -c "$CASES_DIR/extract/extract_scan-directory-01.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/analyze/analyze_resolve-inventory.sh" -c "$CASES_DIR/analyze/analyze_resolve-inventory-01.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/convert/convert_cyclonedx-to-inventory.sh" -c "$CASES_DIR/convert/convert_cyclonedx-to-inventory-01.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/convert/convert_inventory-to-cyclonedx.sh" -c "$CASES_DIR/convert/convert_inventory-to-cyclonedx-01.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/convert/convert_inventory-to-spdx.sh" -c "$CASES_DIR/convert/convert_inventory-to-spdx-01.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/advise/advise_attach-metadata.sh" -c "$CASES_DIR/advise/advise_attach-metadata-01.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/advise/advise_enrich-with-reference.sh" -c "$CASES_DIR/advise/advise_enrich-with-reference-01.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/advise/advise_enrich-inventory.sh" -c "$CASES_DIR/advise/advise_enrich-inventory-01.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/advise/advise_create-dashboard.sh" -c "$CASES_DIR/advise/advise_create-dashboard-01.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/scan/scan_scan-inventory.sh" -c "$CASES_DIR/scan/scan_scan-inventory-01.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/portfolio/portfolio_copy-resources.sh" -c "$CASES_DIR/portfolio/portfolio_copy-resources-01.sh" -f "$LOG_FILE"
# Currently disabled because of unknown NullPointerException
# bash "$PROCESSOR_SCRIPTS_DIR/portfolio/portfolio_create-overview.sh" -c "$CASES_DIR/portfolio/portfolio_create-overview-01.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/report/report_create-document.sh" -c "$CASES_DIR/report/report_create-document-01.sh" -f "$LOG_FILE"
