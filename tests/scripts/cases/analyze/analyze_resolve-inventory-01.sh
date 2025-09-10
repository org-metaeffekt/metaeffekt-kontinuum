#!/bin/bash

export INPUT_INVENTORY_FILE="$PRODUCT_DIR/01_assets/example-001.xlsx"
export OUTPUT_INVENTORY_FILE="$RESOLVED_DIR/example-001-resolved.xlsx"
export DOWNLOAD_BASE_DIR="$TARGET_DIR/maven-index"
export ARTIFACT_RESOLVER_CONFIG="$WORKBENCH_DIR/configs/resolver/artifact-resolver-config.yaml"
export PROXY_CONFIG="$WORKBENCH_DIR/configs/resolver/artifact-resolver-proxy.yaml"