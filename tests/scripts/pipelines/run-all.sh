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

#!/bin/bash

# Exit on any error and treat unset variables as errors
set -euo pipefail

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SELF_DIR/../config.sh"
PROCESSOR_SPECIFIC_SCRIPTS_DIR="$SELF_DIR/../processor-specific"

# Script Pipeline
sh "$PROCESSOR_SPECIFIC_SCRIPTS_DIR/util_create-diff.sh"
sh "$PROCESSOR_SPECIFIC_SCRIPTS_DIR/util_merge-inventories.sh"
sh "$PROCESSOR_SPECIFIC_SCRIPTS_DIR/util_transform-inventories.sh"
sh "$PROCESSOR_SPECIFIC_SCRIPTS_DIR/util_update-mirror.sh"

# requires service
# sh "$PROCESSOR_SPECIFIC_SCRIPTS_DIR/util_portfolio-transfer.sh"

sh "$PROCESSOR_SPECIFIC_SCRIPTS_DIR/util_aggregate-sources.sh"

sh "$PROCESSOR_SPECIFIC_SCRIPTS_DIR/analyze_resolve-inventory.sh"

sh "$PROCESSOR_SPECIFIC_SCRIPTS_DIR/convert_inventory-to-cyclonedx.sh"
sh "$PROCESSOR_SPECIFIC_SCRIPTS_DIR/convert_inventory-to-spdx.sh"

sh "$PROCESSOR_SPECIFIC_SCRIPTS_DIR/advise_attach-metadata.sh"
sh "$PROCESSOR_SPECIFIC_SCRIPTS_DIR/advise_enrich-inventory.sh"
sh "$PROCESSOR_SPECIFIC_SCRIPTS_DIR/advise_create-dashboard.sh"

sh "$PROCESSOR_SPECIFIC_SCRIPTS_DIR/scan_inventory-scanner.sh"
