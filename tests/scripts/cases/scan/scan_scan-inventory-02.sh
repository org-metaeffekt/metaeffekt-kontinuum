#!/bin/bash

export INPUT_INVENTORY_FILE="$RESOLVED_DIR_002/asset-resolved.xlsx"
export OUTPUT_INVENTORY_FILE="$SCANNED_DIR_002/asset-scanned.xlsx"
export ANALYSIS_BASE_DIR="$SCANNED_DIR_002/analysis"
export PROPERTIES_FILE="$INTERNAL_WORKBENCH_DIR/configs/scanner/scan-control.properties"
export ENV_KOSMOS_PASSWORD="EuBsVvcjIElWdXVVtHmPJdsE"
export ENV_KOSMOS_USERKEYS_FILE="$EXTERNAL_WORKBENCH_DIR/config/kosmos/kosmos.consumer.keys"