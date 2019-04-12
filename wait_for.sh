#!/bin/bash


wait_tcp_port() {
    local host="$1" port="$2"

    while ! nc -vz $host $port; do
        echo "waiting for port $host $port"
        sleep 1 # wait for 1/10 of the second before check again
    done
}

wait_tcp_port $1 $2