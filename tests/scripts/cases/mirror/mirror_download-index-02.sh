#!/bin/bash

export MIRROR_TARGET_DIR="${MIRROR_TEST_TARGET_DIR:-/tmp/mirror-download-index-test}"
export MIRROR_ARCHIVE_URL="http://127.0.0.1:${MIRROR_TEST_PORT:-18234}/mirror-index.tar.gz"
export MIRROR_ARCHIVE_USERNAME="mirror_test_user"
export MIRROR_ARCHIVE_PASSWORD="mirror_test_pass_2024"
