#!/bin/bash

# Exit on any error
set -euo pipefail

cat <<"EOF"

------------------------ {metaeffekt} --------------------------
                                                _
                                               | |
             ___  ___   _ __ __   __ ___  _ __ | |_
            / __|/ _ \ | '_ \\ \ / // _ \| '__|| __|
           | (__| (_) || | | |\ V /|  __/| |   | |_
            \___|\___/ |_| |_| \_/  \___||_|    \__|

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
sh "$PROCESSOR_SCRIPTS_DIR/convert/convert_cyclonedx-to-inventory.sh" -o -c "$CASES_DIR/convert/convert_cyclonedx-to-inventory-01.sh" -f "$LOG_FILE" -l "$LOG_LEVEL"
sh "$PROCESSOR_SCRIPTS_DIR/convert/convert_inventory-to-cyclonedx.sh" -o -c "$CASES_DIR/convert/convert_inventory-to-cyclonedx-01.sh" -f "$LOG_FILE" -l "$LOG_LEVEL"
sh "$PROCESSOR_SCRIPTS_DIR/convert/convert_inventory-to-spdx.sh" -o -c "$CASES_DIR/convert/convert_inventory-to-spdx-01.sh" -f "$LOG_FILE" -l "$LOG_LEVEL"
