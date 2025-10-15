#!/bin/bash

export INPUT_POM_FILE="$WORKSPACE_001_DIR/sample-product-1.0.0/01_assets/example-pom.xml"
export OUTPUT_INVENTORY_FILE="$EXTRACTED_DIR_001/extracted-from-pom.xlsx"
export PARAM_INCLUDE_SCOPE_PROVIDED="true"
export PARAM_INCLUDE_SCOPE_SYSTEM="true"
export PARAM_INCLUDE_SCOPE_TEST="true"
export PARAM_INCLUDE_SCOPE_OPTIONAL="true"
export PARAM_INCLUDE_SCOPE_PLUGINS="true"