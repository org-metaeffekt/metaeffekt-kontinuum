#!/bin/bash

# Exit on any error
set -euo pipefail

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"
CASES_DIR="$SELF_DIR/../cases"
LOG_FILE="$SELF_DIR/../../../.logs/$(basename $0).log"

# Maintain script execution order or scripts might break

bash "$PROCESSOR_SCRIPTS_DIR/mirror/mirror_download-index.sh" -f "$LOG_FILE"

bash "$PROCESSOR_SCRIPTS_DIR/prepare/prepare_save-inspect-image.sh" -c "$CASES_DIR/prepare/prepare_save-inspect-image-02.sh" -f "$LOG_FILE"

