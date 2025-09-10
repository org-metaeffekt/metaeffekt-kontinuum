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

# Configuration
SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SELF_DIR/../config.sh"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"
CASES_DIR="$SELF_DIR/../cases"
LOG_FILE=${LOG_FILE:-"$SELF_DIR/../../../.logs/$(basename $0).log"}
LOG_LEVEL=${LOG_LEVEL:-"ALL"}

# Execution of single processors
sh "$PROCESSOR_SCRIPTS_DIR/report/report_create-document.sh" -o -c "$CASES_DIR/report/report_create-document-01.sh" -f "$LOG_FILE" -l "$LOG_LEVEL"

