# Instructions to setup two, two node gemfire clusters to simulate WAN replication

## Installation
* Downloads gemfire 9.1.0 tar.gz package from https://network.pivotal.io/products/pivotal-gemfire. You need to register/login to download.
* Move the tar.gz file into this project's ```archive``` folder.
    * ```mv ~/Downloads/pivotal-gemfire-9.1.0.tar.gz ./archive/```
* ```docker build -t gemfire91 .```
* ```docker-compose up```
    + This sets up two gemfire clusters with WAN replication enabled. For details have a look at docker-compose.yml.
* To ssh into specific containers, run following commands
    * e.g. ssh into server2.
      + ```docker exec -it server2 /bin/bash```
  
## To test network partition.
* ssh into server2 and install and use tc to put network latency of 16 seconds.
    + ```docker exec -it server2 /bin/bash```
    + bash-4.4# ```apk add iproute2```
    + bash-4.4# ```tc qdisc add dev eth0 root netem delay 16000ms```
    + bash-4.4# ```tc -s qdisc```
* Wait for some time. Monitor server2 and locator1 logs. Locator will not receive heart beat 
* and will remove server2 from view, server2 will shutdown but will have reconnect thread running.
* Now remove network latency by following command
    + bash-4.4# ```tc qdisc del dev eth0 root netem```
* Observe network queue clearing up.
    + bash-4.4# ```tc -s qdisc```
* Observe server2 logs. It reconnects to cluster and gets all the new configuration from locator including deployed jars

 
  