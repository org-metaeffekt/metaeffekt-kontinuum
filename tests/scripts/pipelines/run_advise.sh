#!/bin/bash

# Exit on any error
set -euo pipefail

# Configuration
SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SELF_DIR/../config.sh"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"
CASES_DIR="$SELF_DIR/../cases"
LOG_FILE=${LOG_FILE:-"$SELF_DIR/../../../.logs/$(basename $0).log"}
LOG_LEVEL=${LOG_LEVEL:-"CONFIG"}

# Execution of single processors
sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_attach-metadata.sh" -c "$CASES_DIR/advise/advise_attach-metadata-01.sh" \
-f "$LOG_FILE" -l "$LOG_LEVEL"

sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_enrich-with-reference.sh" -c "$CASES_DIR/advise/advise_enrich-with-reference-01.sh" \
-f "$LOG_FILE" -l "$LOG_LEVEL"

sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_enrich-inventory.sh" -c "$CASES_DIR/advise/advise_enrich-inventory-01.sh" \
-f "$LOG_FILE" -l "$LOG_LEVEL"

sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_create-dashboard.sh" -c "$CASES_DIR/advise/advise_create-dashboard-01.sh" \
-f "$LOG_FILE" -l "$LOG_LEVEL"



