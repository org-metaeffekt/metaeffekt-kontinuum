#!/bin/bash

export INPUT_ARCHIVE_FILE="$TESTS_DIR/resources/extraction/windows/sample-windows-extract.tar"
export PARAM_ANALYSIS_DIR="$EXTRACTED_DIR_001/windows-inventory-02/analysis"
export PARAM_EXCLUDE_PATTERN_FILE="$INTERNAL_WORKBENCH_DIR/configs/extractor/exclude-patterns.yaml"
export OUTPUT_INVENTORY_FILE="$EXTRACTED_DIR_001/windows-inventory-02/inventory.xlsx"
export PARAM_KOTLIN_SCRIPT_FILE="$INTERNAL_WORKBENCH_DIR/transformations/hello-world.kts"
export OUTPUT_SCRIPTED_INVENTORY_DIR="$EXTRACTED_DIR_001/windows-inventory-02/scripted"