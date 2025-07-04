#!/bin/bash

# Exit on any error
set -e

# Input configuration
INPUT_FILE='example-001.xlsx'
INPUT_NAME='example-001'

# Source shared configuration
source "$(dirname "${BASH_SOURCE[0]}")/config.sh"

# PREREQUISITES
mkdir -p "$ANALYZED_DIR"
mkdir -p "$RESOLVED_DIR"
mkdir -p "$ADVISED_DIR"
mkdir -p "$SCANNED_DIR"
mkdir -p "$REPORTED_DIR"
mkdir -p "$BOOTSTRAPPED_DIR"
cp $RESOURCES_DIR/$INPUT_FILE "$ANALYZED_DIR/transform"

# TRANSFORM INVENTORY

mvn -f "$PROCESSORS_DIR/bootstrap_transform-inventories.xml" process-resources \
  -Dinput.inventory.dir="$ANALYZED_DIR/transform" \
  -Doutput.inventory.dir="$BOOTSTRAPPED_DIR/transformed" \
  -Dkotlin.script.file="$RESOURCES_DIR/hello-world.kts"

# ATTACH METADATA
mvn -f "$PROCESSORS_DIR/advise_attach-metadata.xml" process-resources \
  -Dinput.inventory="$RESOURCES_DIR/$INPUT_FILE" \
  -Doutput.inventory="$ADVISED_DIR/$INPUT_NAME-attach-metadata.xlsx" \
  -Dmetadata.asset.id="test-asset-id" \
  -Dmetadata.asset.name="test-asset-name" \
  -Dmetadata.asset.version="0.0.1" \
  -Dmetadata.asset.path="test-asset-path" \
  -Dmetadata.asset.type="module"

# CREATE DIFF
mvn -f "$PROCESSORS_DIR/bootstrap_create-diff.xml" process-resources \
  -Dinput.inventory.1="$RESOURCES_DIR/$INPUT_FILE" \
  -Dinput.inventory.2="$ADVISED_DIR/$INPUT_NAME-attach-metadata.xlsx" \
  -Dinventory.1.name="example-001" \
  -Dinventory.2.name="transformed" \
  -Doutput.inventory.dir="$BOOTSTRAPPED_DIR"




