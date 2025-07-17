#!/bin/bash

export INPUT_INVENTORY_FILE="$RESOURCES_DIR/example-001.xlsx"
export OUTPUT_INVENTORY="$ADVISED_DIR/example-001-enriched.xlsx"
export VULNERABILITY_MIRROR_DIR="$UTIL_DIR/mirror/.database"
export SECURITY_POLICY="$RESOURCES_DIR/advise/security-policy.json"
export PROCESSOR_TMP_DIR="$ADVISED_DIR/tmp"
export ACTIVATE_GHSA_CORRELATION="false"
export ACTIVATE_GHSA="false"
export ADDITIONAL_INPUTS_DIR="$RESOURCES_DIR/advise"