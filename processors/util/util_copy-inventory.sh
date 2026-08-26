#!/bin/bash

set -euo pipefail

input_file="$1"
output_file="$2"

if [ ! -f "$input_file" ]; then
  echo "Input file $input_file does not exist."
  exit 1
fi

echo "Creating parent directories of $output_file"
mkdir -p "$(dirname "$output_file")"

echo "Copying $input_file to $output_file"
cp "$input_file" "$output_file"
