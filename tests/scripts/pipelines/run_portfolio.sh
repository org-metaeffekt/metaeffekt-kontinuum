#!/bin/bash

# Exit on any error
set -euo pipefail

cat <<"EOF"

------------------------ {metaeffekt} --------------------------
                            _     __        _  _
                           | |   / _|      | |(_)
        _ __    ___   _ __ | |_ | |_  ___  | | _   ___
       | '_ \  / _ \ | '__|| __||  _|/ _ \ | || | / _ \
       | |_) || (_) || |   | |_ | | | (_) || || || (_) |
       | .__/  \___/ |_|    \__||_|  \___/ |_||_| \___/
       | |
       |_|
-------------------------- kontinuum ---------------------------

EOF

SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_PATH="$SELF_DIR/../config.sh"
PROCESSOR_SCRIPTS_DIR="$SELF_DIR/../processors"

# Portfolio
sh "$PROCESSOR_SCRIPTS_DIR/portfolio/portfolio_copy-resources.sh -p"
# sh "$PROCESSOR_SCRIPTS_DIR/portfolio_create-overview.sh -p" currently disabled

