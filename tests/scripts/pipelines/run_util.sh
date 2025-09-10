#!/bin/bash

# Exit on any error
set -euo pipefail

cat <<"EOF"

------------------------ {metaeffekt} --------------------------
                             _    _  _
                            | |  (_)| |
                      _   _ | |_  _ | |
                     | | | || __|| || |
                     | |_| || |_ | || |
                      \__,_| \__||_||_|

-------------------------- kontinuum ---------------------------

EOF

# Configuration
SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SELF_DIR/../config.sh"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"
CASES_DIR="$SELF_DIR/../cases"
LOG_FILE=${LOG_FILE:-"$SELF_DIR/../../../.logs/$(basename $0).log"}
LOG_LEVEL=${LOG_LEVEL:-"ALL"}

# Execution of single processors
sh "$PROCESSOR_SCRIPTS_DIR/util/util_update-mirror.sh" -o -c "$CASES_DIR/util/util_update-mirror-01.sh" -f "$LOG_FILE" -l "$LOG_LEVEL"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_aggregate-sources.sh" -o -c "$CASES_DIR/util/util_aggregate-sources-01.sh" -f "$LOG_FILE" -l "$LOG_LEVEL"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_copy-inventories.sh" -o -c "$CASES_DIR/util/util_copy-inventories-01.sh" -f "$LOG_FILE" -l "$LOG_LEVEL"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_create-diff.sh" -o -c "$CASES_DIR/util/util_create-diff-01.sh" -f "$LOG_FILE" -l "$LOG_LEVEL"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_merge-inventories.sh" -o -c "$CASES_DIR/util/util_merge-inventories-01.sh" -f "$LOG_FILE" -l "$LOG_LEVEL"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_portfolio-transfer.sh" -o -c "$CASES_DIR/util/util_portfolio-transfer-01.sh" -f "$LOG_FILE" -l "$LOG_LEVEL"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_transform-inventories.sh" -o -c "$CASES_DIR/util/util_transform-inventories-01.sh" -f "$LOG_FILE" -l "$LOG_LEVEL"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_validate-reference-inventory.sh" -o -c "$CASES_DIR/util/util_validate-reference-inventory-01.sh" -f "$LOG_FILE" -l "$LOG_LEVEL"
