package com.demobank.gemfire.repository;

import com.demobank.gemfire.functions.ClientCacheProvider;
import com.demobank.gemfire.functions.DataSeeder;
import com.demobank.gemfire.functions.Page;
import com.demobank.gemfire.functions.TransactionFilterCriteria;
import com.demobank.gemfire.models.Transaction;
import org.apache.geode.pdx.PdxInstance;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RemoteGemfireClientTest {

    @Before
    public void seedData() {
        DataSeeder.main(new String[0]);
    }

    //Make sure to run DataSeeder app before running this test.
    //Transactions and Positions regions are partitioned and can not be programmatically cleared
    @Test
    public void getTransactionsForGivenCriteria() throws InterruptedException {
        while (true) {
            Thread.sleep(200);


            TransactionCache transactionCache = new GemfireTransactionCache(ClientCacheProvider.instance);

            GemfireClient gemfireClient = new GemfireClient(transactionCache);

            //For Remote invocation. The last record has to be always Java object Transaction and not PdxInstance.
            //It will be serialized to server as PdxInstance and server will get that as a PdxInstance.
            //See how nextPage is set in getAllPages method below.
            TransactionFilterCriteria<Transaction> transactionFilterCriteria
                    = new TransactionFilterCriteria(Arrays.asList("9952388700", "8977388888"), Arrays.asList("2020-02-02", "2020-02-03"), 1)
                    .withRecordsPerPage(10);

            List<Page<PdxInstance>> allPages = getAllPages(gemfireClient, transactionFilterCriteria);

            assertEquals(20, allPages.size());

            assertAllPagesAreOfSize(transactionFilterCriteria.getRecordsPerPage(), allPages);

            assertPageSequence(allPages);
        }
    }

    private void assertAllPagesAreOfSize(int recordsPerPage, List<Page<PdxInstance>> allPages) {
        for (Page<PdxInstance> allPage : allPages) {
            assertEquals(allPage.getResults().size(), recordsPerPage);
        }
    }


    private Page<PdxInstance> lastPage(List<Page<PdxInstance>> pageList) {
        return lastRecord(pageList);
    }

    private <T> T lastRecord(List<T> pageList) {
        return pageList.get(pageList.size() - 1);
    }

    private List<Page<PdxInstance>> getAllPages(GemfireClient gemfireClient, TransactionFilterCriteria<Transaction> transactionFilterCriteria) {
        List<Page<PdxInstance>> pageList = new ArrayList();
        Page<PdxInstance> startingPage = gemfireClient.getTransactions(transactionFilterCriteria);
        while (startingPage.getResults().size() >= transactionFilterCriteria.getRecordsPerPage()) {
            TransactionFilterCriteria nextPageCriteria = transactionFilterCriteria.nextPage((Transaction) ((PdxInstance) startingPage.getLastRecord()).getObject());
            long start = System.nanoTime();
            Page<PdxInstance> nextPage = gemfireClient.getTransactions(nextPageCriteria);
            long end = System.nanoTime();
            System.out.println("Time taken for page " + TimeUnit.NANOSECONDS.toMillis(end - start) + " ms");
            pageList.add(startingPage);
            startingPage = nextPage;
        }
        return pageList;
    }


    private void assertPageSequence(List<Page<PdxInstance>> pageList) {
        Page<PdxInstance> firstPage = pageList.get(0);
        BigInteger firstAmount = getAmount(firstPage.firstRecord());
        BigInteger lastPageLastAmount = getAmount(firstPage.lastRecord());
        for (int i = 1; i < pageList.size(); i++) {
            Page<PdxInstance> nextPage = pageList.get(i);
            assertTrue(getAmount(nextPage.firstRecord()).compareTo(lastPageLastAmount) >= 0); //amount sorted in ascending order.

            lastPageLastAmount = getAmount(nextPage.lastRecord());
        }
    }

    private BigInteger getAmount(PdxInstance transactionPdxInstance) {
        return (BigInteger) transactionPdxInstance.getField("amount");
    }


}
