#!/bin/bash
gfsh start server --name=server2 --mcast-port=0 --locators="172.17.0.2[9009]" --server-port=8085 --dir=/data/server2