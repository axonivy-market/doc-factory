#!/bin/bash

mvn --batch-mode versions:set -DnewVersion=${1} -DprocessAllModules
mvn --batch-mode versions:commit -DprocessAllModules
