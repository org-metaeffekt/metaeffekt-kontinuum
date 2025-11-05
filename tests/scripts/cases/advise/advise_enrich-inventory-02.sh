#!/bin/bash

export INPUT_INVENTORY_FILE="$ADVISED_DIR_002/asset-enriched-with-reference.xlsx"
export OUTPUT_INVENTORY_FILE="$ADVISED_DIR_002/asset-enriched.xlsx"
export VULNERABILITY_MIRROR_DIR="$EXTERNAL_VULNERABILITY_MIRROR_DIR/.database"
export SECURITY_POLICY="$EXTERNAL_WORKBENCH_DIR/policies/security-policy.json"
export PROCESSOR_TMP_DIR="$ADVISED_DIR_002/tmp"
export CONTEXT_DIR="$EXTERNAL_WORKBENCH_DIR/contexts"
export ASSESSMENT_DIR="$EXTERNAL_WORKBENCH_DIR/assessments"
export CORRELATION_DIR="$EXTERNAL_WORKBENCH_DIR/correlations"
export ACTIVATE_MSRC="false"
