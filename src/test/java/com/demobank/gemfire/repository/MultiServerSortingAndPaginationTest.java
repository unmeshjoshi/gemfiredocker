package com.demobank.gemfire.repository;

import com.demobank.gemfire.functions.DataGenerator;
import com.demobank.gemfire.functions.Page;
import com.demobank.gemfire.functions.PageBuilder;
import com.demobank.gemfire.functions.TransactionSearchCriteria;
import com.demobank.gemfire.models.Transaction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class StubClient {
    List<StubTransactionCache> servers;

    public StubClient(List<StubTransactionCache> servers) {
        this.servers = servers;
    }

    public Page<Transaction> getPage(TransactionSearchCriteria transactionSearchCriteria) {
        List<Page> pagesFromServer = getPagesFromAllServers(transactionSearchCriteria);
        List<Transaction> allTransactions = mergeAllTransactions(pagesFromServer);
        List<Transaction> sortedTransactions = sortByAmount(allTransactions);

        return new PageBuilder(transactionSearchCriteria.getRecordsPerPage(), sortedTransactions).getPage(1);
    }

    private List<Page> getPagesFromAllServers(TransactionSearchCriteria transactionSearchCriteria) {
        List<Page> pagesFromServer = new ArrayList();
        for (StubTransactionCache cache : servers) {
            Page page = cache.getPage(transactionSearchCriteria);
            pagesFromServer.add(page);
        }
        return pagesFromServer;
    }

    private List<Transaction> sortByAmount(List<Transaction> allTransactions) {
        return allTransactions.stream().sorted((t1, t2) -> t1.getAmount().compareTo(t2.getAmount())).collect(Collectors.toList());
    }

    private List<Transaction> mergeAllTransactions(List<Page> pagesFromServer) {
        return pagesFromServer.stream().flatMap(p -> ((List<Transaction>)p.getResults()).stream()).collect(Collectors.toList());
    }
}

public class MultiServerSortingAndPaginationTest {

    @Test
    public void shouldPaginateResultsOnMultipleServers() {
        StubTransactionCache server1 = new StubTransactionCache();
        StubTransactionCache server2 = new StubTransactionCache();

        DataGenerator dataGenerator = new DataGenerator();
        dataGenerator.seedTransactions(server1,"2020-02-02", "9952388700"); //seeds hundred transactions
        dataGenerator.seedTransactions(server2,"2020-02-03", "8977388700");

        StubClient client = new StubClient(Arrays.asList(server1, server2));

        TransactionSearchCriteria transactionSearchCriteria
                = new TransactionSearchCriteria(Arrays.asList("9952388700", "8977388700"), Arrays.asList("2020-02-02", "2020-02-03"), 1)
                .withRecordsPerPage(10);

        List<Page<Transaction>> pageList = getAllPages(client, transactionSearchCriteria);

        assertEquals(20, pageList.size());

        List<?> results = lastPage(pageList).getResults();
        assertEquals(results.size(), 10);
        Transaction t = (Transaction) lastRecord(results);

        assertPageSequence(pageList);
        assertEquals(Optional.of(199l), Optional.of(t.getTransactionId()));
    }

    private void assertPageSequence(List<Page<Transaction>> pageList) {
        Page<Transaction> firstPage = pageList.get(0);
        Long firstId = firstPage.firstRecord().getTransactionId();
        Long lastId = firstPage.lastRecord().getTransactionId();

        for (int i = 1; i < pageList.size(); i++) {
            Page<Transaction> page = pageList.get(i);
            assertTrue(page.firstRecord().getTransactionId() == lastId + 1);

            lastId = page.lastRecord().getTransactionId();
       }
    }

    private Page<Transaction> lastPage(List<Page<Transaction>> pageList) {
        return (Page<Transaction>)lastRecord(pageList);
    }

    private Object lastRecord(List<?> pageList) {
        return pageList.get(pageList.size() - 1);
    }

    private List<Page<Transaction>> getAllPages(StubClient client, TransactionSearchCriteria transactionSearchCriteria) {
        List<Page<Transaction>> pageList = new ArrayList();
        Page<Transaction> startingPage = client.getPage(transactionSearchCriteria);
        while (startingPage.getResults().size() >= transactionSearchCriteria.getRecordsPerPage()) {
            TransactionSearchCriteria nextPageCriteria = transactionSearchCriteria.nextPage(startingPage.getLastRecord());
            Page nextPage = client.getPage(nextPageCriteria);
            pageList.add(startingPage);
            startingPage = nextPage;
        }
        return pageList;
    }

}