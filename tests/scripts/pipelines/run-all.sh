#!/bin/bash

# Exit on any error
set -euo pipefail

cat <<"EOF"

------------------------ {metaeffekt} --------------------------
 __                    __   .__
|  | __ ____    ____ _/  |_ |__|  ____   __ __  __ __   _____
|  |/ //  _ \  /    \\   __\|  | /    \ |  |  \|  |  \ /     \
|    <(  <_> )|   |  \|  |  |  ||   |  \|  |  /|  |  /|  Y Y  \
|__|_ \\____/ |___|  /|__|  |__||___|  /|____/ |____/ |__|_|  /
     \/            \/                \/                     \/
----------------------- Run All Scripts ------------------------

EOF

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SELF_DIR/../config.sh"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"

# Util
sh "$PROCESSOR_SCRIPTS_DIR/util/util_update-mirror.sh"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_aggregate-sources.sh"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_copy-inventories.sh"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_create-diff.sh"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_merge-filter.sh"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_merge-inventories.sh"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_portfolio-transfer.sh"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_transform-inventories.sh"
sh "$PROCESSOR_SCRIPTS_DIR/util/util_validate-reference-inventory.sh"

# Advise
sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_attach-metadata.sh"
sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_create-dashboard.sh"
sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_enrich-inventory.sh"
sh "$PROCESSOR_SCRIPTS_DIR/advise/advise_enrich-with-reference.sh"

# Analyze
sh "$PROCESSOR_SCRIPTS_DIR/analyze/analyze_resolve-inventory.sh"

# Convert
sh "$PROCESSOR_SCRIPTS_DIR/convert/convert_cyclonedx-to-inventory.sh"
sh "$PROCESSOR_SCRIPTS_DIR/convert/convert_inventory-to-cyclonedx.sh"
sh "$PROCESSOR_SCRIPTS_DIR/convert/convert_inventory-to-spdx.sh"

# Portfolio
sh "$PROCESSOR_SCRIPTS_DIR/portfolio/portfolio_copy-resources.sh"
# sh "$PROCESSOR_SCRIPTS_DIR/portfolio_create-overview.sh" currently disabled

# Scan
sh "$PROCESSOR_SCRIPTS_DIR/scan/scan_scan-inventory.sh"



