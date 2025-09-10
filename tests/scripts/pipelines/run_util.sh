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

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SELF_DIR/../config.sh"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"

# Util
sh "$PROCESSOR_SCRIPTS_DIR/util/util_update-mirror.sh -p"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_aggregate-sources.sh -p"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_copy-inventories.sh -p"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_create-diff.sh -p"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_merge-inventories.sh -p"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_portfolio-transfer.sh -p"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_transform-inventories.sh -p"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_validate-reference-inventory.sh -p"
