#!/bin/bash

export INPUT_INVENTORY_FILE="$WORKSPACE_001_DIR/sample-product-1.0.0/01_assets/example-001.xlsx"
export OUTPUT_INVENTORY_FILE="$SCANNED_DIR_001/example-001-scanned.xlsx"
export ANALYSIS_BASE_DIR="$SCANNED_DIR_001/analysis"
export PROPERTIES_FILE="$EXTERNAL_WORKBENCH_DIR/config/scanner/scan-control.properties"
export ENV_KOSMOS_PASSWORD="EuBsVvcjIElWdXVVtHmPJdsE"
export ENV_KOSMOS_USERKEYS_FILE="$EXTERNAL_WORKBENCH_DIR/config/kosmos/kosmos.consumer.keys"