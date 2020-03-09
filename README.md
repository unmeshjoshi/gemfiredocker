# Instructions to setup two node gemfire cluster to test function execution on partitioned data
# Docker image Setup 
* Downloads gemfire 9.7.3 tgz package from https://network.pivotal.io/products/pivotal-gemfire. You need to register/login to download. Download the tar.gz file in archive folder.
* docker build -t gemfire973 .
# To Run Gemfire Cluster
* ./gradlew clean compileJava jar
* ./startCluster.sh
* To ssh into specific containers, run following commands
* e.g. ssh into server2.
    + ```docker exec -it server2 /bin/bash```
* To execute function on the server, do following
  - Open the project in IntelliJ
  - Run com.demobank.gemfire.functions.DataSeeder
  
    This will setup a set of transactions for two different dates and accounts. This makes sure that data is available on both the servers.
    
  - Run com.demobank.gemfire.functions.RemoteTransactionSearchTest
  
    At this point the test just runs a function to get all the transactions for given accounts and dates.  
# 
# To test network partition.
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

# TBD. To test sync between two separate clusters (for Multi DC setup)

 
  