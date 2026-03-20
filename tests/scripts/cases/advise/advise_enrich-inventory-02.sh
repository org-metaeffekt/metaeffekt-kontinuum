#!/bin/bash

export INPUT_INVENTORY_FILE="$ADVISED_DIR_002/busybox-1.35.0-enriched-with-reference.xlsx"
export OUTPUT_INVENTORY_FILE="$ADVISED_DIR_002/busybox-1.35.0-enriched.xlsx"
export VULNERABILITY_MIRROR_DIR="$EXTERNAL_VULNERABILITY_MIRROR_DIR/.database"
export SECURITY_POLICY="$EXTERNAL_WORKBENCH_DIR/policies/security-policy/security-policy.json"
export PROCESSOR_TMP_DIR="$ADVISED_DIR_002/tmp"
export CORRELATION_DIR="$EXTERNAL_WORKBENCH_DIR/correlations"
export ACTIVATE_MSRC="false"

# Assessments and Context
TENANT_ID="metaeffekt"
ASSET_ID="sample-product-1"
ASSESSMENT_CONTEXT="local"

export CONTEXT_DIR="$EXTERNAL_WORKBENCH_DIR/assessments/$TENANT_ID/$ASSET_ID/$ASSESSMENT_CONTEXT/context"
export ASSESSMENT_DIR="$EXTERNAL_WORKBENCH_DIR/assessments/$TENANT_ID/$ASSET_ID/$ASSESSMENT_CONTEXT/assessments/generic"
