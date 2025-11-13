#!/bin/bash

export INPUT_INVENTORY_FILE="$WORKSPACE_001_DIR/sample-product-1.0.0/01_assets/example-001.xlsx"
export OUTPUT_INVENTORY_FILE="$RESOLVED_DIR_001/example-001-resolved.xlsx"
export DOWNLOAD_BASE_DIR="$KONTINUUM_DIR/.maven-index"
export ARTIFACT_RESOLVER_CONFIG="$EXTERNAL_WORKBENCH_DIR/config/resolver/artifact-resolver-config.yaml"
export PROXY_CONFIG="$EXTERNAL_WORKBENCH_DIR/config/resolver/artifact-resolver-proxy.yaml"