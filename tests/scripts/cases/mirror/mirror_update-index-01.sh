#!/bin/bash

# Supports the full index of all downloaded external data sources

export ENV_MIRROR_DIR="$EXTERNAL_VULNERABILITY_MIRROR_DIR"
export PROCESSOR_POM="mirror/mirror_update-index.xml"

export PARAM_CUSTOM_VULNERABILITY_ACTIVATE="true"
