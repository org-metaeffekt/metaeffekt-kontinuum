#!/bin/bash

export INPUT_INVENTORY_FILE="$ANALYZED_DIR_002/asset-analyzed.xlsx"
export OUTPUT_INVENTORY_FILE="$RESOLVED_DIR_002/asset-resolved.xlsx"
export DOWNLOAD_BASE_DIR="$RESOLVED_DIR_002/maven-index"
export ARTIFACT_RESOLVER_CONFIG="$INTERNAL_WORKBENCH_DIR/configs/resolver/artifact-resolver-config.yaml"
export PROXY_CONFIG="$INTERNAL_WORKBENCH_DIR/configs/resolver/artifact-resolver-proxy.yaml"