package com.demobank.gemfire.repository;

import com.demobank.gemfire.functions.BaseGemfireTest;
import com.demobank.gemfire.functions.DataGenerator;
import com.demobank.gemfire.functions.Page;
import com.demobank.gemfire.functions.TransactionSearchCriteria;
import org.apache.geode.cache.Cache;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class EmbeddedClientTest extends BaseGemfireTest {
    PositionCacheImpl positionCache;
    private TransactionCache transactionCache;

    @Before
    public void seedData() {
        Cache cache = createCache();
        positionCache = new PositionCacheImpl(cache);
        transactionCache = new GemfireTransactionCache(cache);
        DataGenerator dataGenerator = new DataGenerator(positionCache, transactionCache);
        dataGenerator.seedPositions();
        dataGenerator.seedTransactions("2020-02-02", "9952388700");
        dataGenerator.seedTransactions("2020-02-03", "8977388700");
    }

    @Test
    public void shouldGetRequestedPageFromAllTheServerPages() {
        Client client = new Client(transactionCache);

        TransactionSearchCriteria transactionSearchCriteria
                = new TransactionSearchCriteria(Arrays.asList("9952388700", "8977388700"), Arrays.asList("2020-02-02", "2020-02-03"), 1)
                .withRecordsPerPage(10);
        Page firstPage = client.getTransactions(transactionSearchCriteria);
        assertEquals(10, firstPage.getResults().size());
    }

    @Test
    public void shouldGetNextRequestedPageFromAllTheServerPages() {
        Client client = new Client(transactionCache);

        TransactionSearchCriteria transactionSearchCriteria
                = new TransactionSearchCriteria(Arrays.asList("9952388700", "8977388700"), Arrays.asList("2020-02-02", "2020-02-03"), 1)
                .withRecordsPerPage(10);
        Page firstPage = client.getTransactions(transactionSearchCriteria);
        Optional<Object> lastRecord = firstPage.getLastRecord();

        TransactionSearchCriteria forNextPage = transactionSearchCriteria.nextPage(lastRecord);
        Page nextPage = client.getTransactions(forNextPage);

        assertEquals(10, firstPage.getResults().size());
    }
}