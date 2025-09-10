#!/bin/bash

# Exit on any error
set -euo pipefail

cat <<"EOF"

------------------------ {metaeffekt} --------------------------
                         _         _
                        | |       (_)
               __ _   __| |__   __ _  ___   ___
              / _` | / _` |\ \ / /| |/ __| / _ \
             | (_| || (_| | \ V / | |\__ \|  __/
              \__,_| \__,_|  \_/  |_||___/ \___|

-------------------------- kontinuum ---------------------------

EOF

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SELF_DIR/../config.sh"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"

# Advise
sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_attach-metadata.sh -p"
sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_create-dashboard.sh -p"
sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_enrich-inventory.sh -p"
sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_enrich-with-reference.sh -p"

