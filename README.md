# Instructions to run a two node gemfire cluster

* Downloads gemfire 9.1.0 tar.gz package from https://network.pivotal.io/products/pivotal-gemfire. You need to register/login to download. Download the tar.gz file in archive folder.
* docker build -t gemfire91 .
* Open tree tabs and start following three containers
* ```docker run --name locator --hostname=locator -v $(pwd)/data/:/data/ -it gemfire91```
* ```docker run --name server1 --hostname=server1 -v $(pwd)/data/:/data/ -it gemfire91```
* ```docker run --name server2 --hostname=server2 -v $(pwd)/data/:/data/ -it gemfire91```
* In the locator container run following command
  + ```gfsh> start locator --name=locator1 --port=9009 --mcast-port=0 --dir=/data/locator1```
  + Set pdx configuration
    ```gfsh> configure pdx --read-serialized=true```
    ```gfsh> configure pdx --disk-store=DEFAULT```
* In server1 container run following command. MAKE SURE THAT SERVER START/STOP is done from server containers gfsh and not from locator gfsh. Else While starting the server it starts servers on locator node.
  + ```start server --name=server1 --mcast-port=0 --locators="172.17.0.2[9009]" --server-port=8085 --dir=/data/server1```
  + ```connect --locator=172.17.0.2[9009]```
* In server2 container run following command
  + ```start server --name=server2 --mcast-port=0 --locators="172.17.0.2[9009]" --server-port=8085 --dir=/data/server2```  
  + ```connect --locator=172.17.0.2[9009]```
* In the locator container run following command
  + ```create region --name=Positions --type=PARTITION_PERSISTENT --total-num-buckets=7```
  + ```create region --name=MarketPrices --type=PARTITION_PERSISTENT --total-num-buckets=7```
