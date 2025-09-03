#!/bin/bash

export INPUT_INVENTORY_FILE="$PRODUCT_DIR/01_assets/example-001.xlsx"
export OUTPUT_INVENTORY_FILE="$ADVISED_DIR/example-001-enriched.xlsx"
export VULNERABILITY_MIRROR_DIR="$KONTINUUM_DIR/.vulnerability-mirror/.database"
export SECURITY_POLICY="$WORKBENCH_DIR/policies/security-policy.json"
export PROCESSOR_TMP_DIR="$ADVISED_DIR/tmp"
export ACTIVATE_GHSA_CORRELATION="false"
export ACTIVATE_GHSA="false"
export CONTEXT_DIR="$WORKBENCH_DIR/contexts"
export ASSESSMENT_DIR="$WORKBENCH_DIR/assessments"
export CORRELATION_DIR="$WORKBENCH_DIR/correlations"
