#!/bin/bash

set -euo pipefail

if [[ $# -ne 2 ]]; then
  echo "Usage: $0 <input-directory> <output-location>" >&2
  exit 2
fi

input_directory="$1"
output_dir="$2"

if [[ ! -d "$input_directory" ]]; then
  echo "Input directory does not exist: $input_directory" >&2
  exit 1
fi

echo "Creating parent directories of $output_dir"
mkdir -p -- "$(dirname -- "$output_dir")"

echo "Copying directory $input_directory to $output_dir"
cp -R -- "$input_directory" "$output_dir"
