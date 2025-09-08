#!/bin/bash

# Exit on any error
set -euo pipefail

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"
CASES_DIR="$SELF_DIR/../cases"

source "$PROCESSOR_SCRIPTS_DIR/log.sh"

init_logging "$KONTINUUM_DIR/.log/workspace-002.log"

sh "$PROCESSOR_SCRIPTS_DIR/util/util_update-mirror.sh"

sh "$PROCESSOR_SCRIPTS_DIR/analyze/analyze_resolve-inventory.sh" -c "$CASES_DIR/analyze/analyze_resolve-inventory-02.sh" -dl

sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_attach-metadata.sh" -c "$CASES_DIR/advise/advise_attach-metadata-02.sh" -dl
sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_enrich-with-reference.sh" -c "$CASES_DIR/advise/advise_enrich-with-reference-02.sh" -dl
sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_enrich-inventory.sh" -c "$CASES_DIR/advise/advise_enrich-inventory-02.sh" -dl
sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_create-dashboard.sh" -c "$CASES_DIR/advise/advise_create-dashboard-02.sh" -dl
sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_generate-new-dashboard.sh" -c "$CASES_DIR/advise/advise_generate-new-dashboard-02.sh" -dl

sh "$PROCESSOR_SCRIPTS_DIR/scan/scan_scan-inventory.sh" -c "$CASES_DIR/scan/scan_scan-inventory-02.sh" -dl

sh "$PROCESSOR_SCRIPTS_DIR/report/report_create-report.sh" -c "$CASES_DIR/report/report_create-report-02.sh" -dl

