#!/bin/bash

mvn --batch-mode versions:set-property versions:commit -f maven/pom.xml -Dproperty=project-build-plugin-version -DnewVersion=${1}
