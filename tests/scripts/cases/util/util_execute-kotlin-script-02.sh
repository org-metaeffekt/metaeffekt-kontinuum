#!/bin/bash

export INPUT_KOTLIN_SCRIPT_FILE="$INTERNAL_WORKBENCH_DIR/transformations/print-params.kts"
export INPUT_INVENTORY_FILE="$AGGREGATED_DIR_001/sample-asset-1.0/sample-asset-1.0-inventory.xls"
export PARAM_ASSET_ID="sample-asset"
export OUTPUT_INVENTORY_FILE="$AGGREGATED_DIR_001/sample-asset-1.0/sample-asset-1.0-inventory-params.xls"
export PARAM_SCRIPT_PARAMS='{"filter":"os","mode":"strict"}'