#!/bin/bash

docker-compose down
docker rm -f server2 server1 locator1
sudo rm -rf ./data/locator1 ./data/server1 ./data/server2
