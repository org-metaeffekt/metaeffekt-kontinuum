#!/bin/bash

export PORTFOLIO_MANAGER_URL="https://localhost:8443"
export KEYSTORE_CONFIG_FILE="$RESOURCES_DIR/portfolio-manager/client/pm-client-TEST-keystore.p12"
export KEYSTORE_PASSWORD="client-TEST-ks-pw"
export TRUSTSTORE_CONFIG_FILE="$RESOURCES_DIR/portfolio-manager/client/pm-client-TEST-truststore.p12"
export TRUSTSTORE_PASSWORD="client-TEST-ts-pw"
export PRODUCT_NAME="A"
export PRODUCT_VERSION="1.0.0"
export PRODUCT_ARTIFACT_ID="a"
export INPUT_FILE="$RESOURCES_DIR/example-001.xlsx"