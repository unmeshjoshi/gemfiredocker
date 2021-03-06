package com.demobank.gemfire.functions;

import com.demobank.gemfire.repository.GemfireTransactionCache;
import com.demobank.gemfire.repository.PositionCacheImpl;
import com.demobank.gemfire.repository.TransactionCache;
import org.apache.geode.cache.GemFireCache;

public class DataSeeder {
     public static void main(String []args) {
//        should be run only once per cluster setup.
        GemFireCache cache = ClientCacheProvider.instance;
        PositionCacheImpl positionCache = new PositionCacheImpl(cache);
        TransactionCache transactionCache = new GemfireTransactionCache(cache);
        DataGenerator dataGenerator = new DataGenerator();
        dataGenerator.seedPositions("89123891283", positionCache);
        dataGenerator.seedTransactions(transactionCache, "2020-02-02", "9952388700");
        dataGenerator.seedTransactions(transactionCache,"2020-02-03", "8977388888");
    }
}
