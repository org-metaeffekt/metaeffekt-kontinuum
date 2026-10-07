#!/usr/bin/env bash

set -uo pipefail

readonly SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
readonly PROCESSORS_DIR="$SELF_DIR/../processors"

readonly PROCESSOR_TESTS=(
  # artifacts
  artifacts/download-asset.sh
  artifacts/download-maven-artifact.sh
  artifacts/save-container-image.sh

  # assessment
  assessment/convert-assessments.sh
  assessment/merge-assessments.sh

  # bom
  bom/cyclonedx-from-inventory.sh
  bom/cyclonedx-to-inventory.sh
  bom/spdx-from-inventory.sh

  # inventory
  inventory/attach-asset-metadata.sh
  inventory/copy-inventories.sh
  inventory/enrich-inventory-from-reference.sh
  inventory/execute-kotlin-script.sh
  inventory/merge-inventories.sh
  inventory/resolve-inventory.sh
  inventory/scan-artifact-directory.sh
  inventory/transform-inventories.sh
  inventory/validate-reference-inventory.sh

  # licensing
  licensing/aggregate-licenses.sh
  licensing/aggregate-reference-licenses.sh
  licensing/apply-business-case.sh
  licensing/resolve-licensing-information.sh

  # mirror
  mirror/download-data-sources.sh
  mirror/download-index.sh
  mirror/update-index.sh
  mirror/update-index_external.sh

  # portfolio-manager
  portfolio-manager/download-portfolio-manager-jars.sh
  portfolio-manager/portfolio-manager-pull.sh
  portfolio-manager/portfolio-manager-push.sh

  # report
  report/aggregate-source-artifacts.sh
  report/copy-overview-resources.sh
  report/create-annex-archive.sh
  report/create-assessment-dashboard.sh
  report/create-overview.sh
  report/create-report.sh

  # vulnerability
  vulnerability/create-diff.sh
  vulnerability/enrich-advisor-inventory.sh
  vulnerability/enrich-inventory.sh
  vulnerability/generate-report-svg.sh
  vulnerability/merge-advisor-inventories.sh
)

failed_tests=()

for test in "${PROCESSOR_TESTS[@]}"; do
  printf '>>> %s\n' "$test"
  if ! bash "$PROCESSORS_DIR/$test"; then
    failed_tests+=("$test")
  fi
done

if (( ${#failed_tests[@]} > 0 )); then
  printf 'Failed processor tests (%d):\n' "${#failed_tests[@]}" >&2
  printf '  %s\n' "${failed_tests[@]}" >&2
  exit 1
fi

printf 'All %d processor tests passed.\n' "${#PROCESSOR_TESTS[@]}"
