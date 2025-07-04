#!/bin/bash

# Directory paths
SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
PROCESSORS_DIR="$SCRIPT_DIR/../processors"
RESOURCES_DIR="$SCRIPT_DIR/resources"

# Target directories
ANALYZED_DIR="$SCRIPT_DIR/target/analyzed"
RESOLVED_DIR="$SCRIPT_DIR/target/resolved"
ADVISED_DIR="$SCRIPT_DIR/target/advised"
SCANNED_DIR="$SCRIPT_DIR/target/scanned"
REPORTED_DIR="$SCRIPT_DIR/target/reported"
BOOTSTRAPPED_DIR="$SCRIPT_DIR/target/bootstrapped"