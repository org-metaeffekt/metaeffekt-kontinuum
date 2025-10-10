#!/bin/bash

# Exit on any error
set -euo pipefail

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SELF_DIR/../processors/log.sh"
logger_init "INFO" "$SELF_DIR/../../../.logs/run_all.log" "true"

LOG_FILE="$SELF_DIR/../../../.logs/$(basename $0).log"
export LOG_FILE
export LOG_LEVEL="INFO"

sh "$SELF_DIR/run_util.sh"
sh "$SELF_DIR/run_prepare.sh"
sh "$SELF_DIR/run_extract.sh"
sh "$SELF_DIR/run_analyze.sh"
sh "$SELF_DIR/run_convert.sh"
sh "$SELF_DIR/run_advise.sh"
sh "$SELF_DIR/run_portfolio.sh"
sh "$SELF_DIR/run_report.sh"
sh "$SELF_DIR/run_scan.sh"

