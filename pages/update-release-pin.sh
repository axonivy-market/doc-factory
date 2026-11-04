#!/usr/bin/env bash
set -euo pipefail

release_line="${1:?Usage: update-release-pin.sh <release-line> <artifact-version> [pom-file]}"
release_version="${2:?Artifact version is required}"
pom_file="${3:-pages/pom.xml}"

if [[ ! -f "$pom_file" ]]; then
  echo "::error::Pages POM not found: $pom_file"
  exit 1
fi

if [[ "$release_line" =~ ^([0-9]+)\.[0-9]+$ ]]; then
  property="docfactory${BASH_REMATCH[1]}.version"
else
  echo "::error::Unsupported release line: $release_line"
  exit 1
fi

if [[ ! "$release_version" =~ ^[0-9]+\.[0-9]+\.[0-9]+(\.[0-9]+)*$ || "$release_version" != "$release_line".* ]]; then
  echo "::error::Artifact version $release_version must be an official version in release line $release_line."
  exit 1
fi

if ! grep -Fq "<outputDirectory>\${pages.directory}/release/${release_line}</outputDirectory>" "$pom_file"; then
  echo "No Pages pin is configured for release line ${release_line}; skipping."
  if [[ -n "${GITHUB_OUTPUT:-}" ]]; then
    echo "configured=false" >> "$GITHUB_OUTPUT"
  fi
  exit 0
fi

property_count="$(grep -Fc "<${property}>" "$pom_file")"
if [[ "$property_count" -ne 1 ]]; then
  echo "::error::Expected one ${property} property in $pom_file, found ${property_count}."
  exit 1
fi

sed -i "s#<${property}>[^<]*</${property}>#<${property}>${release_version}</${property}>#" "$pom_file"

if ! grep -Fq "<${property}>${release_version}</${property}>" "$pom_file"; then
  echo "::error::Failed to update ${property} to ${release_version}."
  exit 1
fi

echo "Updated release line ${release_line} to ${release_version}."
if [[ -n "${GITHUB_OUTPUT:-}" ]]; then
  printf 'configured=true\nrelease_line=%s\nartifact_version=%s\n' \
    "$release_line" "$release_version" >> "$GITHUB_OUTPUT"
fi
