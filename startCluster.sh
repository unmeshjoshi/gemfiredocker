#!/bin/bash

#Make sure to do a clean build so that latest classes are deployed on the server
./gradlew clean compileJava jar

docker-compose up -d