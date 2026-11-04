#!/usr/bin/env bash
set -euo pipefail

release_tag="${1:?Usage: update-release-pin.sh <release-tag> [pom-file]}"
pom_file="${2:-pages/pom.xml}"

if [[ ! -f "$pom_file" ]]; then
  echo "::error::Pages POM not found: $pom_file"
  exit 1
fi

if [[ "$release_tag" =~ ^v?([0-9]+)\.([0-9]+)\.[0-9]+$ ]]; then
  release_version="${release_tag#v}"
  release_line="${BASH_REMATCH[1]}.${BASH_REMATCH[2]}"
  property="docfactory${BASH_REMATCH[1]}.version"
else
  echo "::error::Unsupported release tag format: $release_tag"
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
