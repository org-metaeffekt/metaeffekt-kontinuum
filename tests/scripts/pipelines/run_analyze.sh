#!/bin/bash

# Exit on any error
set -euo pipefail

cat <<"EOF"

------------------------ {metaeffekt} --------------------------
                                 _
                                | |
             __ _  _ __    __ _ | | _   _  ____ ___
            / _` || '_ \  / _` || || | | ||_  // _ \
           | (_| || | | || (_| || || |_| | / /|  __/
            \__,_||_| |_| \__,_||_| \__, |/___|\___|
                                     __/ |
                                    |___/
-------------------------- kontinuum ---------------------------

EOF

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SELF_DIR/../config.sh"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"

# Analyze
sh "$PROCESSOR_SCRIPTS_DIR/analyze/analyze_resolve-inventory.sh"
