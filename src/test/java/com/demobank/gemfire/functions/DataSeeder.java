package com.demobank.gemfire.functions;

import com.demobank.gemfire.repository.PositionCache;
import com.demobank.gemfire.repository.TransactionCache;
import org.apache.geode.cache.GemFireCache;

public class DataSeeder {
     public static void main(String []args) {
//        should be run only once per cluster setup.
        GemFireCache cache = ClientCacheProvider.instance;
        PositionCache positionCache = new PositionCache(cache);
        TransactionCache transactionCache = new TransactionCache(cache);
        DataGenerator dataGenerator = new DataGenerator(positionCache, transactionCache);
        dataGenerator.seedPositions();
        dataGenerator.seedTransactions("2020-02-02", "9952388700");
        dataGenerator.seedTransactions("2020-02-03", "8977388888");
    }
}
