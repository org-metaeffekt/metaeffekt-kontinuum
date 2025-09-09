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

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SELF_DIR/../config.sh"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"

# Convert
sh "$PROCESSOR_SCRIPTS_DIR/convert/convert_cyclonedx-to-inventory.sh -p"
sh "$PROCESSOR_SCRIPTS_DIR/convert/convert_inventory-to-cyclonedx.sh -p"
sh "$PROCESSOR_SCRIPTS_DIR/convert/convert_inventory-to-spdx.sh -p"
