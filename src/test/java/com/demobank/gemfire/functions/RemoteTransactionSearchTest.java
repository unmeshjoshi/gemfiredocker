package com.demobank.gemfire.functions;

import com.demobank.gemfire.repository.PositionCache;
import com.demobank.gemfire.repository.TransactionCache;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.pdx.PdxInstance;
import org.junit.Before;
import org.junit.Test;

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
        new DataGenerator(positionCache, transactionCache).seedData();
    }

    @Test
    public void getTransactionsForGivenCriteria() {
        TransactionSearchCriteria transactionSearchCriteria
                = new TransactionSearchCriteria("9952388700", "2020-02-02", 1);
        List<PdxInstance> transactions = transactionCache.getTransactions(transactionSearchCriteria);
        assertEquals(100, transactions.size());
    }
}
