package com.demobank.gemfire.functions;

import com.demobank.gemfire.repository.GemfireTransactionCache;
import com.demobank.gemfire.repository.PositionCacheImpl;
import com.demobank.gemfire.repository.TransactionCache;
import org.apache.geode.cache.Cache;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EmbeddedTransactionSearchTest extends BaseGemfireTest {
    PositionCacheImpl positionCache;
    private TransactionCache transactionCache;

    @Before
    public void seedData() {
        Cache cache = createCache();
        positionCache = new PositionCacheImpl(cache);
        transactionCache = new GemfireTransactionCache(cache);
        DataGenerator dataGenerator = new DataGenerator();
        dataGenerator.seedPositions("acctKey", positionCache);
        dataGenerator.seedTransactions(transactionCache, "2020-02-02", "9952388700");
        dataGenerator.seedTransactions(transactionCache, "2020-02-03", "8977388700");
    }

    @Test
    public void getTransactionsForGivenCriteria() {
        TransactionFilterCriteria transactionFilterCriteria
                = new TransactionFilterCriteria(Arrays.asList("9952388700", "8977388700"), Arrays.asList("2020-02-02", "2020-02-03"), 1)
                    .withRecordsPerPage(10);

        List<Page> transactions = transactionCache.getTransactions(transactionFilterCriteria);
        assertEquals(1, transactions.size());
        assertEquals(10, transactions.get(0).getResults().size());
    }
}
