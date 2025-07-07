#!/bin/bash

set -euo pipefail

# Define base directories
SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
export PROCESSORS_DIR="$SELF_DIR/../../processors"
export RESOURCES_DIR="$SELF_DIR/../resources"
export CASES_DIR="$SELF_DIR/cases"

# Define target directories
TARGET_BASE_DIR="$SELF_DIR/target"
ANALYZED_DIR="$TARGET_BASE_DIR/analyzed"
RESOLVED_DIR="$TARGET_BASE_DIR/resolved"
ADVISED_DIR="$TARGET_BASE_DIR/advised"
SCANNED_DIR="$TARGET_BASE_DIR/scanned"
REPORTED_DIR="$TARGET_BASE_DIR/reported"
UTIL_DIR="$TARGET_BASE_DIR/util"
CONVERTED_DIR="$TARGET_BASE_DIR/converted"

# Create all target directories
echo "Creating target directories..."
if ! mkdir -p "$CONVERTED_DIR" "$ANALYZED_DIR" "$RESOLVED_DIR" "$ADVISED_DIR" "$SCANNED_DIR" "$REPORTED_DIR" "$UTIL_DIR"; then
    echo "Error: Failed to create target directories" >&2
    exit 1
fi

echo "Configuration loaded successfully"