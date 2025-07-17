#!/bin/bash

export INPUT_INVENTORY_FILE="$RESOURCES_DIR/example-003.xlsx"
export ANNEX_SOURCE_DIR="$TARGET_BASE_DIR/annex/sources"
export RETAINED_SOURCES_DIR="$TARGET_BASE_DIR/util/aggregate-sources"
export ALTERNATIVE_ARTIFACT_SOURCE_MAPPING=""
export INCLUDE_ALL_SOURCES="true"
export FAIL_ON_MISSING_SOURCES="true"
export TARGET_DIR="$TARGET_BASE_DIR/util/aggregate-sources"
