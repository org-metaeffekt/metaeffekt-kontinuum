#!/bin/bash

export INPUT_INVENTORY_FILE="$EXTRACTED_DIR_002/keycloak-21.1-extracted.xlsx"
export OUTPUT_INVENTORY_FILE="$RESOLVED_DIR_002/asset-resolved.xlsx"
export DOWNLOAD_BASE_DIR="$KONTINUUM_DIR/.maven-index"
export ARTIFACT_RESOLVER_CONFIG="$INTERNAL_WORKBENCH_DIR/configs/resolver/artifact-resolver-config.yaml"
export PROXY_CONFIG="$INTERNAL_WORKBENCH_DIR/configs/resolver/artifact-resolver-proxy.yaml"