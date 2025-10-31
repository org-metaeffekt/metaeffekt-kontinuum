#!/bin/bash

# Exit on any error
set -euo pipefail

# Configuration
SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SHARED_SCRIPT_PATH="$SELF_DIR/../shared.sh"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"
CASES_DIR="$SELF_DIR/../cases"
LOG_FILE=${LOG_FILE:-"$SELF_DIR/../../../.logs/$(basename $0).log"}


# Execution of single processors
sh "$PROCESSOR_SCRIPTS_DIR/prepare/prepare_copy-pom-dependencies.sh" -c "$CASES_DIR/prepare/prepare_copy-pom-dependencies-01.sh" -f "$LOG_FILE"

# requires docker
sh "$PROCESSOR_SCRIPTS_DIR/prepare/prepare_save-inspect-image.sh" -c "$CASES_DIR/prepare/prepare_save-inspect-image-01.sh" -f "$LOG_FILE"
