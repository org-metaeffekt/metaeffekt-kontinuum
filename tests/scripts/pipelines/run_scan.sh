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

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SELF_DIR/../config.sh"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"

# Scan
sh "$PROCESSOR_SCRIPTS_DIR/scan/scan_scan-inventory.sh -p"
