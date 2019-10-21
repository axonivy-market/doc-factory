#!/bin/bash

rm -rf build
mvn clean package
(sleep 20 && firefox http://localhost:8000) &
docker-compose up
