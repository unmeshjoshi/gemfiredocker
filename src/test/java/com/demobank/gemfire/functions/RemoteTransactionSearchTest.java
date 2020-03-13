package com.demobank.gemfire.functions;

import com.demobank.gemfire.repository.GemfireTransactionCache;
import com.demobank.gemfire.repository.TransactionCache;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RemoteTransactionSearchTest extends BaseGemfireTest {
    //Make sure to run DataSeeder app before running this test.
    //Transactions and Positions regions are partitioned and can not be programmatically cleared
    @Test
    public void getTransactionsForGivenCriteria() {
        TransactionCache transactionCache = new GemfireTransactionCache(ClientCacheProvider.instance);
        TransactionFilterCriteria transactionFilterCriteria
                = new TransactionFilterCriteria(Arrays.asList("9952388700", "8977388888"), Arrays.asList("2020-02-02", "2020-02-03"), 1)
                .withRecordsPerPage(10);

        List<Page> pagesFromServers = transactionCache.getTransactions(transactionFilterCriteria);
        assertEquals(2, pagesFromServers.size());

        assertEquals(10, pagesFromServers.get(0).getResults().size());
        assertEquals(10, pagesFromServers.get(1).getResults().size());
    }
}
