#!/bin/bash

docker-compose down

rm -rf ./data/locator1 ./data/server1 ./data/server2

docker-compose up -d