package com.demobank.gemfire.repository;

import com.demobank.gemfire.functions.Page;
import com.demobank.gemfire.functions.PageBuilder;
import com.demobank.gemfire.functions.TransactionField;
import com.demobank.gemfire.functions.TransactionSearchCriteria;

import java.util.List;
import java.util.stream.Collectors;

class GemfireClient<T> {
    private TransactionCache<T> transactionCache;

    public GemfireClient(TransactionCache<T> transactionCache) {
        this.transactionCache = transactionCache;
    }

    public Page<T> getTransactions(TransactionSearchCriteria criteria) {
        List<Page<T>> pagesFromServers = transactionCache.getTransactions(criteria);
        List<T> allTransactions = mergeAllRecords(pagesFromServers);
        List<T> sortedTransactions = sortBy(allTransactions, criteria.getSortByField());
        //always pick up first page after collecting from all the servers. Each server gives records for one page each.
        return pickFirstPage(sortedTransactions, criteria.getRecordsPerPage());
    }


    private Page pickFirstPage(List<T> sortedTransactions, int recordsPerPage) {
        return new PageBuilder(recordsPerPage, sortedTransactions).firstPage();
    }
    private List<T> sortBy(List<T> allTransactions, TransactionField sortByField) {
        return allTransactions.stream().sorted(sortByField.getComparator()).collect(Collectors.toList());
    }

    private List<T> mergeAllRecords(List<Page<T>> pagesFromServers) {
        return pagesFromServers.stream().flatMap(p -> p.getResults().stream()).collect(Collectors.toList());
    }
}
