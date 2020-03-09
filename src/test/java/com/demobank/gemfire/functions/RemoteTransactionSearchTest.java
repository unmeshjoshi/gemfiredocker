package com.demobank.gemfire.functions;

import com.demobank.gemfire.repository.PositionCache;
import com.demobank.gemfire.repository.TransactionCache;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.pdx.PdxInstance;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RemoteTransactionSearchTest extends BaseGemfireTest {
    PositionCache positionCache;
    private TransactionCache transactionCache;

    @Before
    public void seedData() {
        GemFireCache cache = ClientCacheProvider.instance;
        positionCache = new PositionCache(cache);
        transactionCache = new TransactionCache(cache);

//        should be run only once per cluster setup.
//        DataGenerator dataGenerator = new DataGenerator(positionCache, transactionCache);
//        dataGenerator.seedPositions();
//        dataGenerator.seedTransactions("2020-02-02", "9952388700");
//        dataGenerator.seedTransactions("2020-02-03", "8977388888");

    }

    @Test
    public void getTransactionsForGivenCriteria() {
        TransactionSearchCriteria transactionSearchCriteria
                = new TransactionSearchCriteria(Arrays.asList("9952388700", "8977388888"), Arrays.asList("2020-02-02", "2020-02-03"), 1);
        List<PdxInstance> transactions = transactionCache.getTransactions(transactionSearchCriteria);
        assertEquals(200, transactions.size());
    }
}
