package com.demobank.gemfire.functions;

import com.demobank.gemfire.repository.PositionCache;
import com.demobank.gemfire.repository.TransactionCache;
import org.apache.geode.cache.Cache;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EmbeddedTransactionSearchTest extends BaseGemfireTest {
    PositionCache positionCache;
    private TransactionCache transactionCache;

    @Before
    public void seedData() {
        Cache cache = createCache();
        positionCache = new PositionCache(cache);
        transactionCache = new TransactionCache(cache);
        DataGenerator dataGenerator = new DataGenerator(positionCache, transactionCache);
        dataGenerator.seedPositions();
        dataGenerator.seedTransactions("2020-02-02", "9952388700");
        dataGenerator.seedTransactions("2020-02-03", "8977388700");
    }

    @Test
    public void getTransactionsForGivenCriteria() {
        TransactionSearchCriteria transactionSearchCriteria
                = new TransactionSearchCriteria(Arrays.asList("9952388700", "8977388700"), Arrays.asList("2020-02-02", "2020-02-03"), 1)
                    .withRecordsPerPage(10);

        List<Page> transactions = transactionCache.getTransactions(transactionSearchCriteria);
        assertEquals(1, transactions.size());
        assertEquals(10, transactions.get(0).results.size());
    }
}
