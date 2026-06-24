#!/bin/bash

export INPUT_INVENTORY_FILE="$WORKSPACE_001_DIR/sample-product-1.0.0/01_assets/example-001.xlsx"
export OUTPUT_ARTIFACTS_DIR="$AGGREGATED_DIR_001"
export PARAM_PROPERTY_FILE="$EXTERNAL_WORKBENCH_DIR/config/source-aggregation/source-aggregation.properties"
export PARAM_INCLUDE_ALL_SOURCES="false"
export PARAM_FAIL_ON_MISSING_SOURCES="false"
