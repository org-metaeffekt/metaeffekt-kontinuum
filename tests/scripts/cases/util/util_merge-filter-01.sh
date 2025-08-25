#!/bin/bash

export INPUT_REFERENCE_INVENTORIES_DIR="$RESOURCES_DIR/advise/reference-inventory"
export OUTPUT_FILTERED_INVENTORY_FILE="$UTIL_DIR/merged-filtered.xlsx"
export PARAM_VULNERABILITY_ADVISORY_FILTER="[{'name': 'CERT_FR'}]"
export INPUT_SECURITY_POLICY_FILE="$RESOURCES_DIR/advise/security-policy.json"

