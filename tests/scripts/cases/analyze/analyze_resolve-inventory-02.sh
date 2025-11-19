#!/bin/bash

export INPUT_INVENTORY_FILE="$EXTRACTED_DIR_002/busybox-1.35.0-extracted.xlsx"
export OUTPUT_INVENTORY_FILE="$RESOLVED_DIR_002/busybox-1.35.0-resolved.xlsx"
export DOWNLOAD_BASE_DIR="$KONTINUUM_DIR/.maven-index"
export ARTIFACT_RESOLVER_CONFIG="$EXTERNAL_WORKBENCH_DIR/config/resolver/artifact-resolver-config.yaml"
export PROXY_CONFIG="$EXTERNAL_WORKBENCH_DIR/config/resolver/artifact-resolver-proxy.yaml"