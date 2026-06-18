#!/bin/bash

# Exit on any error
set -euo pipefail

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# bash "$SELF_DIR/001_complete.sh"

source "$SELF_DIR/../../../external.rc"
find "$LOCAL_MAVEN_REPO" -type f -size +2400k -print | sed "s|^$LOCAL_MAVEN_REPO/||" | sort > "$SELF_DIR/../../../.logs/external-file-report.log"
