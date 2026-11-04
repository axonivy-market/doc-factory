#!/bin/sh
set -eu

repo_root=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)
cd "$repo_root"

mvn -U --batch-mode -f pages/pom.xml clean generate-resources -DincludeDev=true
exec java -m jdk.httpserver -b 127.0.0.1 -p 8000 -d "$repo_root/pages/target"
