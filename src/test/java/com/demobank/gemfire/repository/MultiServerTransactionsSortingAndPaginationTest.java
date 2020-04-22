package com.demobank.gemfire.repository;

import com.demobank.gemfire.functions.DataGenerator;
import com.demobank.gemfire.functions.Page;
import com.demobank.gemfire.functions.TransactionFilterCriteria;
import com.demobank.gemfire.models.Transaction;
import com.demobank.gemfire.models.TransactionKey;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class StubClientSideTransactionCache implements TransactionCache<Transaction>  {
    List<StubTransactionCache> servers;

    public StubClientSideTransactionCache(List<StubTransactionCache> servers) {
        this.servers = servers;
    }


    @Override
    public void add(TransactionKey key, List<Transaction> transactions) {
        //noop
    }

    @Override
    public List<Page<Transaction>> getTransactions(TransactionFilterCriteria criteria) {
        return servers.stream().flatMap(server -> server.getTransactions(criteria).stream()).collect(Collectors.toList());
    }

    @Override
    public void clear() {

    }
}

public class MultiServerTransactionsSortingAndPaginationTest {

    @Test
    public void shouldPaginateResultsOnMultipleServers() {
        StubTransactionCache server1 = new StubTransactionCache();
        StubTransactionCache server2 = new StubTransactionCache();

        DataGenerator dataGenerator = new DataGenerator();
        dataGenerator.seedTransactions(server1,"2020-02-02", "9952388700"); //seeds hundred transactions
        dataGenerator.seedTransactions(server2,"2020-02-03", "8977388700");

        GemfireClient gemfireClient = new GemfireClient(new StubClientSideTransactionCache(Arrays.asList(server1, server2)));

        TransactionFilterCriteria transactionFilterCriteria
                = new TransactionFilterCriteria(Arrays.asList("9952388700", "8977388700"), Arrays.asList("2020-02-02", "2020-02-03"), 1)
                .withRecordsPerPage(10);

        List<Page<Transaction>> pageList = getAllPages(gemfireClient, transactionFilterCriteria);

        assertEquals(20, pageList.size());

        List<Transaction> results = lastPage(pageList).getResults();
        assertEquals(results.size(), 10);

        assertPageSequence(pageList);
    }

    private Page<Transaction> lastPage(List<Page<Transaction>> pageList) {
        return lastRecord(pageList);
    }

    private <T> T lastRecord(List<T> pageList) {
        return pageList.get(pageList.size() - 1);
    }

    private List<Page<Transaction>> getAllPages(GemfireClient gemfireClient, TransactionFilterCriteria transactionFilterCriteria) {
        List<Page<Transaction>> pageList = new ArrayList();
        Page<Transaction> startingPage = gemfireClient.getTransactions(transactionFilterCriteria);
        while (startingPage.getResults().size() >= transactionFilterCriteria.getRecordsPerPage()) {
            TransactionFilterCriteria nextPageCriteria = transactionFilterCriteria.nextPage(startingPage.getLastRecord());
            Page nextPage = gemfireClient.getTransactions(nextPageCriteria);
            pageList.add(startingPage);
            startingPage = nextPage;
        }
        return pageList;
    }

    private void assertPageSequence(List<Page<Transaction>> pageList) {
        Page<Transaction> firstPage = pageList.get(0);
        BigInteger firstAmount = firstPage.firstRecord().getAmount();
        BigInteger lastPageLastAmount = firstPage.lastRecord().getAmount();

        for (int i = 1; i < pageList.size(); i++) {
            Page<Transaction> nextPage = pageList.get(i);
            assertTrue(nextPage.firstRecord().getAmount().compareTo(lastPageLastAmount) >= 0); //amount sorted in ascending order.
            lastPageLastAmount = nextPage.lastRecord().getAmount();
        }
    }

}