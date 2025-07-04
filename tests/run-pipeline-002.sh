#!/bin/bash

# Exit on any error
set -e

# Input configuration
INPUT_FILE='example-002.xlsx'
INPUT_NAME='example-002'

# Source shared configuration
source "$(dirname "${BASH_SOURCE[0]}")/config.sh"

# PREREQUISITES
mkdir -p "$ANALYZED_DIR"
mkdir -p "$RESOLVED_DIR"
mkdir -p "$ADVISED_DIR"
mkdir -p "$SCANNED_DIR"
mkdir -p "$REPORTED_DIR"
mkdir -p "$BOOTSTRAPPED_DIR"

# MERGE INVENTORIES
mvn -f "$PROCESSORS_DIR/bootstrap_merge-inventories.xml" process-resources \
  -Dinput.inventory.dir="$RESOURCES_DIR" \
  -Dinventory.includes="*.xlsx" \
  -Doutput.inventory="$BOOTSTRAPPED_DIR/merged.xlsx"

# SCAN INVENTORY
mvn -f "$PROCESSORS_DIR/scanned_inventory-scanner.xml" process-resources \
  -Dinput.inventory="$BOOTSTRAPPED_DIR/merged.xlsx" \
  -Doutput.inventory="$SCANNED_DIR/merged-scanned.xlsx" \
  -Danalysis.base.dir="$SCANNED_DIR/analysis" \
  -Dproperties.file="$RESOURCES_DIR/scan-control.properties"

# INVENTORY TO CYCLONEDX
mvn -f "$PROCESSORS_DIR/convert_inventory-to-spdx.xml" process-resources \
  -Dinput.inventory="$SCANNED_DIR/merged-scanned.xlsx" \
  -Doutput.bom="$SCANNED_DIR/merged-spdx.json" \
  -Ddocument.name="test-document" \
  -Ddocument.description="test-document-description" \
  -Ddocument.organization="{metaeffekt}" \
  -Ddocument.organization.url="https://metaeffekt.com" \
  -Doutput.format="JSON" \
  -Ddocument.id.prefix="ae-kontinuum-"
