#!/bin/bash

export INPUT_INVENTORY_FILE="$ANALYZED_DIR/sample-asset-1.0/sample-asset-1.0-inventory.xls"
export DOCUMENT_NAME="test-document"
export DOCUMENT_DESCRIPTION="test-document-description"
export DOCUMENT_ORGANIZATION="{metaeffekt} GmbH"
export DOCUMENT_ORGANIZATION_URL="https://metaeffekt.com"
export OUTPUT_BOM_FILE="$CONVERTED_DIR/sample-asset-1.0-cyclonedx.json"
export OUTPUT_FORMAT="JSON"