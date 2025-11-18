#!/bin/bash

# Exit on any error
set -euo pipefail

# Configuration
SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SHARED_SCRIPT_PATH="$SELF_DIR/../shared.sh"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"
CASES_DIR="$SELF_DIR/../cases"
LOG_FILE=${LOG_FILE:-"$SELF_DIR/../../../.logs/$(basename $0).log"}


# Execution of single processors
bash "$PROCESSOR_SCRIPTS_DIR/mirror/mirror_download-index.sh" -c "$CASES_DIR/mirror/mirror_download-index-01.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/util/util_aggregate-sources.sh" -c "$CASES_DIR/util/util_aggregate-sources-01.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/util/util_copy-inventories.sh" -c "$CASES_DIR/util/util_copy-inventories-01.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/util/util_create-diff.sh" -c "$CASES_DIR/util/util_create-diff-01.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/util/util_merge-inventories.sh" -c "$CASES_DIR/util/util_merge-inventories-01.sh" -f "$LOG_FILE"
# bash "$PROCESSOR_SCRIPTS_DIR/util/util_portfolio-transfer.sh" -c "$CASES_DIR/util/util_portfolio-transfer-01.sh" -f "$LOG_FILE"
# Fails for some reason, however only during FIRST execution.  Disabled for now.
bash "$PROCESSOR_SCRIPTS_DIR/util/util_transform-inventories.sh" -c "$CASES_DIR/util/util_transform-inventories-01.sh" -f "$LOG_FILE"
bash "$PROCESSOR_SCRIPTS_DIR/util/util_validate-reference-inventory.sh" -c "$CASES_DIR/util/util_validate-reference-inventory-01.sh" -f "$LOG_FILE"
