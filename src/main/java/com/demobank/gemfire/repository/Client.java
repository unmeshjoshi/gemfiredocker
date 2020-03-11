package com.demobank.gemfire.repository;

import com.demobank.gemfire.functions.Page;
import com.demobank.gemfire.functions.PageBuilder;
import com.demobank.gemfire.functions.TransactionSearchCriteria;

import java.util.List;
import java.util.stream.Collectors;

class Client<T> {
    private TransactionCache<T> transactionCache;

    public Client(TransactionCache<T> transactionCache) {
        this.transactionCache = transactionCache;
    }

    public Page<T> getTransactions(TransactionSearchCriteria criteria) {
        List<Page<T>> pagesFromServers = transactionCache.getTransactions(criteria);
        List<T> allRecords = mergeAllRecords(pagesFromServers);
        return createClientPage(allRecords, criteria.getRecordsPerPage(), criteria.getRequestedPage());
    }

    private Page createClientPage(List<T> allRecords, int recordsPerPage, int requestedPage) {
        return new PageBuilder(recordsPerPage, allRecords).getPage(requestedPage);
    }

    private List<T> mergeAllRecords(List<Page<T>> pagesFromServers) {
        return pagesFromServers.stream().flatMap(p -> p.getResults().stream()).collect(Collectors.toList());
    }
}
