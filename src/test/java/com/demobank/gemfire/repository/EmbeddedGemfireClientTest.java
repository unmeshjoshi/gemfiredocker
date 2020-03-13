package com.demobank.gemfire.repository;

import com.demobank.gemfire.functions.BaseGemfireTest;
import com.demobank.gemfire.functions.DataGenerator;
import com.demobank.gemfire.functions.Page;
import com.demobank.gemfire.functions.TransactionFilterCriteria;
import org.apache.geode.cache.Cache;
import org.apache.geode.pdx.PdxInstance;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EmbeddedGemfireClientTest extends BaseGemfireTest {
    private static PositionCacheImpl positionCache;
    private static TransactionCache transactionCache;

    @BeforeClass
    public static void seedData() {
        Cache cache = createCache();
        positionCache = new PositionCacheImpl(cache);
        transactionCache = new GemfireTransactionCache(cache);
        DataGenerator dataGenerator = new DataGenerator();
        dataGenerator.seedPositions("acctKey", positionCache);
        dataGenerator.seedTransactions(transactionCache, "2020-02-02", "9952388700");
        dataGenerator.seedTransactions(transactionCache, "2020-02-03", "8977388700");
    }

    @Test
    public void shouldGetRequestedPageFromAllTheServerPages() {
        GemfireClient gemfireClient = new GemfireClient(transactionCache);

        TransactionFilterCriteria transactionFilterCriteria
                = new TransactionFilterCriteria(Arrays.asList("9952388700", "8977388700"), Arrays.asList("2020-02-02", "2020-02-03"), 1)
                .withRecordsPerPage(10);
        Page firstPage = gemfireClient.getTransactions(transactionFilterCriteria);
        assertEquals(10, firstPage.getResults().size());
    }

    @Test
    public void shouldGetNextRequestedPageFromAllTheServerPages() {
        GemfireClient gemfireClient = new GemfireClient(transactionCache);

        TransactionFilterCriteria<PdxInstance> transactionFilterCriteria
                = new TransactionFilterCriteria(Arrays.asList("9952388700", "8977388700"), Arrays.asList("2020-02-02", "2020-02-03"), 1)
                .withRecordsPerPage(10);
        Page<PdxInstance> firstPage = gemfireClient.getTransactions(transactionFilterCriteria);
        PdxInstance lastRecord = firstPage.getLastRecord();

        TransactionFilterCriteria forNextPage = transactionFilterCriteria.nextPage(lastRecord);
        Page<PdxInstance> nextPage = gemfireClient.getTransactions(forNextPage);

        assertEquals(10, nextPage.getResults().size());
        assertTrue(((BigInteger)firstPage.lastRecord().getField("amount")).compareTo((BigInteger)(nextPage.firstRecord().getField("amount"))) <= 0);
    }
}