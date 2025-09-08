#!/bin/bash

# Exit on any error
set -euo pipefail

cat <<"EOF"

------------------------ {metaeffekt} --------------------------
                                             _
                                            | |
              _ __  ___  _ __    ___   _ __ | |_
             | '__|/ _ \| '_ \  / _ \ | '__|| __|
             | |  |  __/| |_) || (_) || |   | |_
             |_|   \___|| .__/  \___/ |_|    \__|
                        | |
                        |_|
-------------------------- kontinuum ---------------------------

EOF

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SELF_DIR/../config.sh"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"

# Report
sh "$PROCESSOR_SCRIPTS_DIR/report/report_create-overview.sh"

