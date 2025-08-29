#!/bin/bash

export INPUT_REFERENCE_INVENTORIES_DIR="$WORKBENCH_DIR/inventories/example-reference-inventory"
export OUTPUT_FILTERED_INVENTORY_FILE="$UTIL_DIR/merged-filtered.xlsx"
export PARAM_VULNERABILITY_ADVISORY_FILTER="[{'name': 'CERT_FR'}]"
export INPUT_SECURITY_POLICY_FILE="$WORKBENCH_DIR/policies/security-policy.json"

