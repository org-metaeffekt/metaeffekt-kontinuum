#!/bin/bash

# Exit on any error
set -euo pipefail

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SELF_DIR/../config.sh"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"

sh "$SELF_DIR/run_util.sh"
sh "$SELF_DIR/run_analyze.sh"
sh "$SELF_DIR/run_convert.sh"
sh "$SELF_DIR/run_advise.sh"
sh "$SELF_DIR/run_portfolio.sh"
sh "$SELF_DIR/run_report.sh"
sh "$SELF_DIR/run_scan.sh"

