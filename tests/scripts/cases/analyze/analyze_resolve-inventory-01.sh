#!/bin/bash

export INPUT_INVENTORY_FILE="$RESOURCES_DIR/example-001.xlsx"
export OUTPUT_INVENTORY_FILE="$RESOLVED_DIR/example-001-resolved.xlsx"
export DOWNLOAD_BASE_DIR="$TARGET_DIR/maven-index"
export ARTIFACT_RESOLVER_CONFIG="$RESOURCES_DIR/artifact-resolver-config.yaml"
export PROXY_CONFIG="$RESOURCES_DIR/artifact-resolver-proxy.yaml"