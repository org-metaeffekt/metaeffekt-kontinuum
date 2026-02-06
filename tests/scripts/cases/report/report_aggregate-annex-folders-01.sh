#!/bin/bash

export INPUT_INVENTORY_FILE="$EXTERNAL_WORKBENCH_DIR/inventories/synthetic-reference-inventory/inventory/artifact-inventory-01.xls"
export PARAM_REFERENCE_INVENTORY_DIR="$EXTERNAL_WORKBENCH_DIR/inventories/synthetic-reference-inventory/inventory"
export PARAM_REFERENCE_INVENTORY_INCLUDES="**/*.ser,**/*.xls,**/*.xlsx"
export PARAM_REFERENCE_COMPONENT_PATH="components"
export PARAM_REFERENCE_LICENSE_PATH="licenses"
export PARAM_TARGET_COMPONENT_DIR="/Users/romeo/metaeffekt/Repositories/metaeffekt-kontinuum/tests/target/workspace-001/sample-product-1.0.0/08_reported/components"
export PARAM_TARGET_LICENSE_DIR="/Users/romeo/metaeffekt/Repositories/metaeffekt-kontinuum/tests/target/workspace-001/sample-product-1.0.0/08_reported/licenses"
export PARAM_FAIL_ON_MISSING_LICENSE_FILE="false"