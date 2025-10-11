#!/bin/bash

# Exit on any error
set -euo pipefail

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SELF_DIR/../processors/log.sh"
logger_init "INFO" "$SELF_DIR/../../../.logs/run_all.log" "true"

LOG_FILE="$SELF_DIR/../../../.logs/$(basename $0).log"
export LOG_FILE
export LOG_LEVEL="CONFIG"

bash "$SELF_DIR/run_util.sh"
bash "$SELF_DIR/run_prepare.sh"
bash "$SELF_DIR/run_extract.sh"
bash "$SELF_DIR/run_analyze.sh"
bash "$SELF_DIR/run_convert.sh"
bash "$SELF_DIR/run_advise.sh"
bash "$SELF_DIR/run_portfolio.sh"
bash "$SELF_DIR/run_report.sh"
bash "$SELF_DIR/run_scan.sh"
