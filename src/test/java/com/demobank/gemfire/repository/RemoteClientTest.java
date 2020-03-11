package com.demobank.gemfire.repository;

import com.demobank.gemfire.functions.ClientCacheProvider;
import com.demobank.gemfire.functions.Page;
import com.demobank.gemfire.functions.TransactionSearchCriteria;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class RemoteClientTest {
    //Make sure to run DataSeeder app before running this test.
    //Transactions and Positions regions are partitioned and can not be programmatically cleared
    @Test
    public void getTransactionsForGivenCriteria() {
        TransactionCache transactionCache = new GemfireTransactionCache(ClientCacheProvider.instance);
        Client client = new Client(transactionCache);

        TransactionSearchCriteria transactionSearchCriteria
                = new TransactionSearchCriteria(Arrays.asList("9952388700", "8977388888"), Arrays.asList("2020-02-02", "2020-02-03"), 1)
                .withRecordsPerPage(10);

        Page clientPage = client.getTransactions(transactionSearchCriteria);

        assertEquals(10, clientPage.getResults().size());
    }
}
