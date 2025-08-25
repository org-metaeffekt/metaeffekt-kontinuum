#!/bin/bash

export INPUT_INVENTORY_FILE="$RESOURCES_DIR/example-001.xlsx"
export DOCUMENT_NAME="test-document"
export DOCUMENT_DESCRIPTION="test-document-description"
export DOCUMENT_ORGANIZATION="{metaeffekt} GmbH"
export DOCUMENT_ORGANIZATION_URL="https://metaeffekt.com"
export OUTPUT_BOM_FILE="$CONVERTED_DIR/example-001-converted_cyclonedx.json"
export OUTPUT_FORMAT="JSON"