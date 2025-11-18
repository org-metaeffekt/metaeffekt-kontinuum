#!/bin/bash

# Exit on any error
set -euo pipefail

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SELF_DIR/../processors/log.sh"
logger_init "$SELF_DIR/../../../.logs/run_workspace-001.log"

LOG_FILE="$SELF_DIR/../../../.logs/$(basename $0).log"
export LOG_FILE

bash "$SELF_DIR/run_util.sh"
bash "$SELF_DIR/run_prepare.sh"
bash "$SELF_DIR/run_extract.sh"
bash "$SELF_DIR/run_analyze.sh"
bash "$SELF_DIR/run_convert.sh"
bash "$SELF_DIR/run_advise.sh"
bash "$SELF_DIR/run_portfolio.sh"
bash "$SELF_DIR/run_report.sh"

# Disabled due to a dependency on a running scancode service
# bash "$SELF_DIR/run_scan.sh"
