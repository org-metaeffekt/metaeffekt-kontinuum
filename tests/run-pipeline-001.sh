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

# RESOLVE INVENTORY
mvn -f "$PROCESSORS_DIR/analyze_resolve-inventory.xml" process-resources \
  -Dinput.inventory="$RESOURCES_DIR/$INPUT_FILE" \
  -Doutput.inventory="$RESOLVED_DIR/$INPUT_NAME-resolved.xlsx" \
  -DdownloadBaseDir="$RESOLVED_DIR/download" \
  -DartifactResolverConfig="$RESOURCES_DIR/artifact-resolver-config.yaml" \
  -DproxyConfig="$RESOURCES_DIR/artifact-resolver-proxy.yaml"

# UPDATE VULNERABILITY MIRROR
mvn -f "$PROCESSORS_DIR/bootstrap_update-mirror.xml" compile -P withoutProxy \
  -Dmirror.target.dir="$BOOTSTRAPPED_DIR/mirror" \
  -Dmirror.archive.url="http://ae-scanner/mirror/index/index-database.zip" \
  -Dmirror.archive.name="index-database.zip"

# ENRICH INVENTORY
mvn -f "$PROCESSORS_DIR/advise_enrich-inventory.xml" process-resources \
  -Dinput.inventory="$RESOLVED_DIR/$INPUT_NAME-resolved.xlsx" \
  -Doutput.inventory="$ADVISED_DIR/$INPUT_NAME-advised.xlsx" \
  -Dvulnerability.mirror.dir="$BOOTSTRAPPED_DIR/mirror/.database" \
  -Dsecurity.policy="$RESOURCES_DIR/security-policy.json" \
  -Dprocessor.tmp.dir="$ADVISED_DIR/tmp" \
  -Dactivate.ghsa.correlation="false" \
  -Dactivate.ghsa="false"

# INVENTORY TO CYCLONEDX
mvn -f "$PROCESSORS_DIR/convert_inventory-to-cyclonedx.xml" process-resources \
  -Dinput.inventory="$ADVISED_DIR/$INPUT_NAME-advised.xlsx" \
  -Doutput.bom="$ADVISED_DIR/$INPUT_NAME-cyclonedx.json" \
  -Ddocument.name="test-document" \
  -Ddocument.description="test-document-description" \
  -Ddocument.organization="{metaeffekt}" \
  -Ddocument.organization.url="https://metaeffekt.com" \
  -Doutput.format="JSON"

# CREATE DASHBOARD
mvn -f "$PROCESSORS_DIR/advise_create-dashboard.xml" process-resources \
  -Dinput.inventory="$ADVISED_DIR/$INPUT_NAME-advised.xlsx" \
  -Doutput.dashboard="$ADVISED_DIR/$INPUT_NAME-dashboard.html" \
  -Dvulnerability.mirror.dir="$BOOTSTRAPPED_DIR/mirror/.database" \
  -Dsecurity.policy="$RESOURCES_DIR/security-policy.json"