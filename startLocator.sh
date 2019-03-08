#!/bin/bash
gfsh start locator --name=locator1 --port=9009 --mcast-port=0 --dir=/data/locator1
echo "waiting for locator to start"
sleep 20
echo "Configuring pdx disk store"
gfsh -e "connect --locator=172.17.0.2[9009]" -e "create disk-store --name=PDX_TYPES --dir=pdx_types"
echo "Configuring pdx"
gfsh -e "connect --locator=172.17.0.2[9009]" -e "configure pdx --read-serialized=true --disk-store=PDX_TYPES"
echo "Creating Position region"
gfsh -e "connect --locator=172.17.0.2[9009]" -e "create region --name=Positions --type=PARTITION_PERSISTENT --total-num-buckets=7"
