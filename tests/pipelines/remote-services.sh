#!/usr/bin/env bash

set -uo pipefail

readonly SELF_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
readonly PROCESSORS_DIR="$SELF_DIR/../processors"

readonly PROCESSOR_TESTS=(
  # requires remote databases or repositories
  artifacts/download-asset.sh
  artifacts/download-maven-artifact.sh
  artifacts/save-container-image.sh
  inventory/resolve-inventory.sh

  # requires vulnerability mirror
  mirror/download-data-sources.sh
  mirror/download-index.sh
  mirror/update-index.sh
  mirror/update-index_external.sh

  # requires portfolio manager
  portfolio-manager/download-portfolio-manager-jars.sh
  portfolio-manager/portfolio-manager-pull.sh
  portfolio-manager/portfolio-manager-push.sh
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
