#!/bin/bash

# Exit on any error
set -euo pipefail

cat <<"EOF"

------------------------ {metaeffekt} --------------------------
                    ___   ___  __ _  _ __
                    / __| / __|/ _` || '_ \
                    \__ \| (__| (_| || | | |
                    |___/ \___|\__,_||_| |_|

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
sh "$PROCESSOR_SCRIPTS_DIR/scan/scan_scan-inventory.sh" -o -c "$CASES_DIR/scan/scan_scan-inventory-01.sh" -f "$LOG_FILE" -l "$LOG_LEVEL"
