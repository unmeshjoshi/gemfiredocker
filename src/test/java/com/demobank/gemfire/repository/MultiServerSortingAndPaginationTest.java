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
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

class StubClient {
    List<StubTransactionCache> servers;

    public StubClient(List<StubTransactionCache> servers) {
        this.servers = servers;
    }

    public Page<Transaction> getPage(TransactionSearchCriteria transactionSearchCriteria) {
        List<Page> pagesFromServer = getPagesFromAllServers(transactionSearchCriteria);
        List<Transaction> allTransactions = mergeAllTransactions(pagesFromServer);
        List<Transaction> sortedTransactions = sortByAmount(allTransactions, transactionSearchCriteria);
        //always pick up first page after collecting from all the servers. Each server gives records for one page each.
        return pickFirstPage(transactionSearchCriteria, sortedTransactions);
    }

    private Page pickFirstPage(TransactionSearchCriteria transactionSearchCriteria, List<Transaction> sortedTransactions) {
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

    private List<Transaction> sortByAmount(List<Transaction> allTransactions, TransactionSearchCriteria transactionSearchCriteria) {
        return allTransactions.stream().sorted(transactionSearchCriteria.getSortByField().getComparator()).collect(Collectors.toList());
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

        List<Transaction> results = lastPage(pageList).getResults();
        assertEquals(results.size(), 10);
    }

    private Page<Transaction> lastPage(List<Page<Transaction>> pageList) {
        return lastRecord(pageList);
    }

    private <T> T lastRecord(List<T> pageList) {
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