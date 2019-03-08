#!/bin/bash
gfsh start server --name=server1 --mcast-port=0 --locators=172.17.0.2[9009] --server-port=8085 --dir=/data/server1

gfsh -e "connect --locator=172.17.0.2[9009]"  -e "stop server --name=server1 --mcast-port=0 --locators="172.17.0.2[9009]" --server-port=8085 --dir=/data/server1"