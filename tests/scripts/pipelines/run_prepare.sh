#!/bin/bash

# Exit on any error
set -euo pipefail

# Configuration
SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SELF_DIR/../config.sh"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"
CASES_DIR="$SELF_DIR/../cases"
LOG_FILE=${LOG_FILE:-"$SELF_DIR/../../../.logs/$(basename $0).log"}
LOG_LEVEL=${LOG_LEVEL:-"CONFIG"}

# Execution of single processors
sh "$PROCESSOR_SCRIPTS_DIR/prepare/prepare_copy-pom-dependencies.sh" -c "$CASES_DIR/prepare/prepare_copy-pom-dependencies-01.sh" -f "$LOG_FILE" -l "$LOG_LEVEL"
sh "$PROCESSOR_SCRIPTS_DIR/prepare/prepare_save-inspect-image.sh" -c "$CASES_DIR/prepare/prepare_save-inspect-image-01.sh" -f "$LOG_FILE" -l "$LOG_LEVEL"
