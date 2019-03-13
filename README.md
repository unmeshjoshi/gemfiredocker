# Instructions to run a two node gemfire cluster

* Downloads gemfire 9.1.0 tar.gz package from https://network.pivotal.io/products/pivotal-gemfire. You need to register/login to download. Download the tar.gz file in archive folder.
* docker build -t gemfire91 .
* Open tree tabs and start following three containers
* ```docker run --name locator1 --hostname=locator1 -v $(pwd)/data/:/data/ -it gemfire91 /bin/bash```
* ```docker run --name server1 --hostname=server1 -v $(pwd)/data/:/data/  --cap-add=NET_ADMIN -it gemfire91 /bin/bash```
* ```docker run --name server2 --hostname=server2 -v $(pwd)/data/:/data/ --cap-add=NET_ADMIN -it gemfire91 /bin/bash```
* In the locator container run following command
  + ```gfsh```
  + ```gfsh> start locator --name=locator1 --port=9009 --mcast-port=0 --properties-file=/pivotal-gemfire-9.1.0/config/gemfire.properties   --dir=/data/locator1```
* In server1 container run following command. MAKE SURE THAT SERVER START/STOP is done from server containers gfsh and not from locator gfsh. Else While starting the server it starts servers on locator node.
  + ```gfsh```
  + ```start server --name=server1 --mcast-port=0 --locators="172.17.0.2[9009]" --server-port=8085 --properties-file=/pivotal-gemfire-9.1.0/config/gemfire.properties --dir=/data/server1```
* In server2 container run following command
  + ```gfsh```
  + ```start server --name=server2 --mcast-port=0 --locators="172.17.0.2[9009]" --server-port=8085 --properties-file=/pivotal-gemfire-9.1.0/config/gemfire.properties  --dir=/data/server2```  
* In the locator container run following command
  + If you want separate disk store, Create disk store
     ```gfsh> create disk-store --name=PDX_TYPES --dir=pdx_types```
  + Set pdx configuration
     ```gfsh> configure pdx --disk-store=DEFAULT --read-serialized=true --auto-serializable-classes=com.gemfire.functions.*,com.gemfire.models.*```
  + ```gfsh> create region --name=Positions --type=PARTITION_PERSISTENT --total-num-buckets=7```
  + ```gfsh> create region --name=MarketPrices --type=PARTITION_PERSISTENT --total-num-buckets=7```
  + ```gfsh> create region --name=FxRates --type=PARTITION_PERSISTENT --total-num-buckets=7```
* In server1 container run following command to restart server1.
  + ```gfsh> connect --locator=172.17.0.2[9009]```
  + ```gfsh> stop server --name=server1```
  + ```gfsh> start server --name=server1 --mcast-port=0 --locators="172.17.0.2[9009]" --server-port=8085 --dir=/data/server1```
  + ```gfsh> connect --locator=172.17.0.2[9009]```
* In server2 container run following command to restart server2.
  + ```gfsh> connect --locator=172.17.0.2[9009]```
  + ```gfsh> stop server --name=server2```
  + ```gfsh> start server --name=server2 --mcast-port=0 --locators="172.17.0.2[9009]" --server-port=8085 --dir=/data/server2```
  + ```gfsh> connect --locator=172.17.0.2[9009]```  
  
  * To test network partition.
  * ssh into server2 and install and use tc to put network latency of 16 seconds.
    + ```docker exec -it server2 /bin/bash```
    + ```bash-4.4# apk add iproute2```
    + ```bash-4.4# tc qdisc add dev eth0 root netem delay 16000ms```
    + ```bash-4.4# tc -s qdisc```
    * Wait for some time. Monitor server2 and locator1 logs. Locator will not receive heart beat 
    * and will remove server2 from view, server2 will shutdown but will have reconnect thread running.
    * Now remove network latency by following command
    + ```bash-4.4# tc qdisc del dev eth0 root netem```
    * Observe network queue clearing up.
    + ```bash-4.4# tc -s qdisc```
    * Observe server2 logs. It reconnects to cluster and gets all the new configuration from locator including deployed jars
 