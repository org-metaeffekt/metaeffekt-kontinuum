#!/usr/bin/env bash
# Usage: download-maven-artifacts.sh <groupId> [artifactId] <version> <outputDir> [repoUrl]
#
# If artifactId is empty, downloads all artifacts under groupId with the given version.
# If artifactId is set, downloads only that specific artifact.
# Downloads both .pom and .jar files for each artifact.
# Uses the Maven repository's directory listing (not the search API) to discover artifacts,
# so it works even when the search index is stale.
set -e

GROUP_ID="$1"
ARTIFACT_ID="$2"
VERSION="$3"
OUTPUT_DIR="$4"
REPO_URL="${5:-https://repo1.maven.org/maven2}"

GROUP_PATH=$(echo "$GROUP_ID" | tr '.' '/')
mkdir -p "$OUTPUT_DIR"

download_artifact_files() {
  local aid="$1"
  local base_url="${REPO_URL}/${GROUP_PATH}/${aid}/${VERSION}"
  local pom_url="${base_url}/${aid}-${VERSION}.pom"
  local jar_url="${base_url}/${aid}-${VERSION}.jar"

  if curl -fsI "$pom_url" | head -1 | grep -q "200"; then
    echo "Downloading $pom_url"
    curl -fL -o "${OUTPUT_DIR}/${aid}-${VERSION}.pom" "$pom_url"
  fi

  if curl -fsI "$jar_url" | head -1 | grep -q "200"; then
    echo "Downloading $jar_url"
    curl -fL -o "${OUTPUT_DIR}/${aid}-${VERSION}.jar" "$jar_url"
  fi
}

if [ -n "$ARTIFACT_ID" ]; then
  download_artifact_files "$ARTIFACT_ID"
else
  GROUP_URL="${REPO_URL}/${GROUP_PATH}/"
  echo "Listing artifacts in $GROUP_URL"
  ARTIFACT_DIRS=$(curl -fsSL "$GROUP_URL" | grep -o 'href="[a-zA-Z0-9][a-zA-Z0-9._-]*/"' | sed 's/href="//;s/\/"//' | sort -u)

  if [ -z "$ARTIFACT_DIRS" ]; then
    echo "No artifacts found in group $GROUP_ID at $GROUP_URL"
    exit 1
  fi

  for AID in $ARTIFACT_DIRS; do
    VERSION_DIR_URL="${REPO_URL}/${GROUP_PATH}/${AID}/${VERSION}/"
    if curl -fsI "$VERSION_DIR_URL" | head -1 | grep -q "200"; then
      download_artifact_files "$AID"
    fi
  done
fi
