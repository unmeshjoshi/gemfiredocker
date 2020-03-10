package com.demobank.gemfire.repository;

import com.demobank.gemfire.functions.Page;
import com.demobank.gemfire.functions.PageBuilder;
import com.demobank.gemfire.functions.TransactionSearchCriteria;

import java.util.List;
import java.util.stream.Collectors;

class Client {
    private TransactionCache transactionCache;

    public Client(TransactionCache transactionCache) {
        this.transactionCache = transactionCache;
    }

    public Page getTransactions(TransactionSearchCriteria criteria) {
        List<Page> pagesFromServers = transactionCache.getTransactions(criteria);
        List<?> allRecords = mergeAllRecords(pagesFromServers);
        return createClientPage(allRecords, criteria.getRecordsPerPage(), criteria.getRequestedPage());
    }

    private Page createClientPage(List<?> allRecords, int recordsPerPage, int requestedPage) {
        return new PageBuilder(recordsPerPage, allRecords).getPage(requestedPage);
    }

    private List<?> mergeAllRecords(List<Page> pagesFromServers) {
        return pagesFromServers.stream().flatMap(p -> p.getResults().stream()).collect(Collectors.toList());
    }
}
