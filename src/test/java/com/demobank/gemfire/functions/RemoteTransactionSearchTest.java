package com.demobank.gemfire.functions;

import com.demobank.gemfire.repository.TransactionCache;
import org.apache.geode.pdx.PdxInstance;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RemoteTransactionSearchTest extends BaseGemfireTest {

    @Before
    public void seedData() {
        DataSeeder.main(new String[0]);
    }
    //Make sure to run DataSeeder app before running this test.
    //Transactions and Positions regions are partitioned and can not be programmatically cleared
    @Test
    public void getTransactionsForGivenCriteria() {
        TransactionCache transactionCache = new TransactionCache(ClientCacheProvider.instance);
        TransactionSearchCriteria transactionSearchCriteria
                = new TransactionSearchCriteria(Arrays.asList("9952388700", "8977388888"), Arrays.asList("2020-02-02", "2020-02-03"), 1)
                .withRecordsPerPage(10);

        List<Page> pagesFromServers = transactionCache.getTransactions(transactionSearchCriteria);
        assertEquals(2, pagesFromServers.size());

        Page firstPage = pagesFromServers.get(0);
        PdxInstance firstRecord = (PdxInstance) firstPage.getResults().get(0);
        String accountNumber = (String) firstRecord.getField("accountNumber");
        assertEquals(accountNumber, "9952388700");

        assertEquals(10, pagesFromServers.get(0).results.size());
        assertEquals(10, pagesFromServers.get(1).results.size());
    }
}
