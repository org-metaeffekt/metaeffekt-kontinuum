#!/usr/bin/env bash
# Usage: download-maven-artifacts.sh <groupId> [artifactId] <version> <outputDir> [repoUrl]
#
# If artifactId is empty, downloads all artifacts under groupId with the given version.
# If artifactId is set, downloads only that specific artifact.
set -e

GROUP_ID="$1"
ARTIFACT_ID="$2"
VERSION="$3"
OUTPUT_DIR="$4"
REPO_URL="$5"

GROUP_PATH=$(echo "$GROUP_ID" | tr '.' '/')
mkdir -p "$OUTPUT_DIR"

if [ -n "$ARTIFACT_ID" ]; then
  ARTIFACT_URL="${REPO_URL}/${GROUP_PATH}/${ARTIFACT_ID}/${VERSION}/${ARTIFACT_ID}-${VERSION}.jar"
  echo "Downloading $ARTIFACT_URL"
  curl -fL -o "${OUTPUT_DIR}/${ARTIFACT_ID}-${VERSION}.jar" "$ARTIFACT_URL"
else
  SEARCH_URL="https://search.maven.org/solrsearch/select?q=g:${GROUP_ID}+AND+v:${VERSION}&rows=500&wt=json"
  echo "Searching for artifacts: $SEARCH_URL"
  ARTIFACT_IDS=$(curl -fsSL "$SEARCH_URL" | grep -o '"a":"[^"]*"' | sed 's/"a":"//;s/"//')
  if [ -z "$ARTIFACT_IDS" ]; then
    echo "No artifacts found for group=$GROUP_ID version=$VERSION"
    exit 1
  fi
  for AID in $ARTIFACT_IDS; do
    ARTIFACT_URL="${REPO_URL}/${GROUP_PATH}/${AID}/${VERSION}/${AID}-${VERSION}.jar"
    echo "Downloading $ARTIFACT_URL"
    curl -fL -o "${OUTPUT_DIR}/${AID}-${VERSION}.jar" "$ARTIFACT_URL" || echo "  (skipped $AID, no jar)"
  done
fi
