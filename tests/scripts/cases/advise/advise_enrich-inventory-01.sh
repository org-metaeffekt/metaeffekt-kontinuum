#!/bin/bash

export INPUT_INVENTORY_FILE="$WORKSPACE_001_DIR/sample-product-1.0.0/01_assets/example-001.xlsx"
export OUTPUT_INVENTORY_FILE="$ADVISED_DIR_001/example-001-enriched.xlsx"
export VULNERABILITY_MIRROR_DIR="$KONTINUUM_DIR/.mirror/.database"
export SECURITY_POLICY="$WORKBENCH_DIR/policies/security-policy.json"
export PROCESSOR_TMP_DIR="$ADVISED_DIR_001/tmp"
export CONTEXT_DIR="$WORKBENCH_DIR/contexts"
export ASSESSMENT_DIR="$WORKBENCH_DIR/assessments"
export CORRELATION_DIR="$WORKBENCH_DIR/correlations"
