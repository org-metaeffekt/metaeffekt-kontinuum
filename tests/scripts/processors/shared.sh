#!/bin/bash

set -eo pipefail

# Print usage information
print_usage() {
    cat << EOF
Usage: $0 [options]
    -c <case>   : Which case to select for running the test. Either an absolute
                  path or relative to the cases directory.
    -f          : The path to the .log file for the running script
    -h          : Show this help message

Example:
    $0 -c /path/to/case -f /path/to/log-file
EOF
}