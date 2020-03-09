package com.demobank.gemfire.functions;

import com.demobank.gemfire.repository.TransactionCache;
import org.apache.geode.pdx.PdxInstance;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RemoteTransactionSearchTest extends BaseGemfireTest {

    //Make sure to run DataSeeder app before running this test.
    //Transactions and Positions regions are partitioned and can not be programmatically cleared
    @Test
    public void getTransactionsForGivenCriteria() {
        TransactionCache transactionCache = new TransactionCache(ClientCacheProvider.instance);
        TransactionSearchCriteria transactionSearchCriteria
                = new TransactionSearchCriteria(Arrays.asList("9952388700", "8977388888"), Arrays.asList("2020-02-02", "2020-02-03"), 1);
        List<PdxInstance> transactions = transactionCache.getTransactions(transactionSearchCriteria);
        assertEquals(200, transactions.size());
    }
}
