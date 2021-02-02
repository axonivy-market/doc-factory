#!/bin/bash

mvn versions:set-property versions:commit -f maven/pom.xml -Dproperty=project-build-plugin-version -DnewVersion=${1}
