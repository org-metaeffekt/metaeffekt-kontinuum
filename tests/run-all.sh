#!/bin/bash

# Exit on any error
set -e
cat <<"EOF"

------------------------ {metaeffekt} --------------------------
 __                    __   .__
|  | __ ____    ____ _/  |_ |__|  ____   __ __  __ __   _____
|  |/ //  _ \  /    \\   __\|  | /    \ |  |  \|  |  \ /     \
|    <(  <_> )|   |  \|  |  |  ||   |  \|  |  /|  |  /|  Y Y  \
|__|_ \\____/ |___|  /|__|  |__||___|  /|____/ |____/ |__|_|  /
     \/            \/                \/                     \/
--------------------- Run All Pipelines ------------------------

EOF

RUN_PARALLEL="false" # Experimental

if [ $RUN_PARALLEL == "true" ]; then
    source "$(dirname "${BASH_SOURCE[0]}")/run-pipeline-001.sh" &
    source "$(dirname "${BASH_SOURCE[0]}")/run-pipeline-002.sh" &
    source "$(dirname "${BASH_SOURCE[0]}")/run-pipeline-003.sh" &

    wait
else
    source "$(dirname "${BASH_SOURCE[0]}")/run-pipeline-001.sh"
    source "$(dirname "${BASH_SOURCE[0]}")/run-pipeline-002.sh"
    source "$(dirname "${BASH_SOURCE[0]}")/run-pipeline-003.sh"
fi

