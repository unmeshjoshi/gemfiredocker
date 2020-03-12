package com.demobank.gemfire.repository;

import com.demobank.gemfire.functions.Page;
import com.demobank.gemfire.functions.PageBuilder;
import com.demobank.gemfire.functions.TransactionField;
import com.demobank.gemfire.functions.TransactionSearchCriteria;
import com.demobank.gemfire.models.Transaction;
import com.demobank.gemfire.models.TransactionKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StubTransactionCache implements TransactionCache<Transaction> {
    Map<TransactionKey, List<Transaction>> transactions = new HashMap<>();

    public int getTotalLength() {
        return transactions.values().stream().collect(Collectors.toList()).size();
    }

    @Override
    public void add(TransactionKey key, List<Transaction> transactions) {
        this.transactions.put(key, transactions);
    }

    @Override
    public List<Page<Transaction>> getTransactions(TransactionSearchCriteria criteria) {
        List<Page<Transaction>> pages = new ArrayList();
        pages.add(getPage(criteria));
        return pages;
    }

    public int getTotalRecords(TransactionSearchCriteria criteria) {
        List<Transaction> result = getAllTransactionsFor(criteria);
        return result.size();
    }

    @Override
    public void clear() {
        transactions.clear();
    }


    public Page getPage(TransactionSearchCriteria criteria) {
        //sort by amount
        List<Transaction> result = getAllTransactionsFor(criteria);

        TransactionField sortByField = criteria.getSortByField();

        List<Transaction> sortedTransactions = result.stream().sorted(sortByField.getComparator()).collect(Collectors.toList());
        if (criteria.getRequestedPage() == 1) {
           return firstPage(criteria, sortedTransactions);
        }

        return getNextPageFromLastRecord(criteria, sortedTransactions);
    }

    private Page getNextPageFromLastRecord(TransactionSearchCriteria criteria, List<Transaction> sortedTransactions) {
        Object lastRecord = criteria.getLastRecord();
        Transaction lastTransactionFromPreviousPage = (Transaction) lastRecord;
        int index = firstIndexMoreThanOrEqual(criteria.getSortByField(), sortedTransactions, lastTransactionFromPreviousPage);
        if (index == -1) {
            return new Page(criteria.getRequestedPage(), new ArrayList(), -1);
        }
        return new PageBuilder(criteria.getRecordsPerPage(), sortedTransactions).getPageStartingAt(criteria.getRequestedPage(), index);
    }

    private Page firstPage(TransactionSearchCriteria criteria, List<Transaction> sortedTransactions) {
        return new PageBuilder(criteria.getRecordsPerPage(), sortedTransactions).getPage(1);
    }

    private int firstIndexMoreThanOrEqual(TransactionField sortByField, List<Transaction> sortedTransactions, Transaction lastTransactionFromPreviousPage) {
        for (int i = 0; i < sortedTransactions.size(); i++) {
             if (sortByField.getComparator().compare(sortedTransactions.get(i), lastTransactionFromPreviousPage) > 0) {
                   return i;
             }
        }
        return -1;
    }

    private List<Transaction> getAllTransactionsFor(TransactionSearchCriteria criteria) {
        List<TransactionKey> keys = criteria.getKeys();
        List<Transaction> result = new ArrayList();
        for (TransactionKey key : keys) {
            List<Transaction> transactions = this.transactions.get(key);
            if (transactions != null) {
                result.addAll(transactions);
            }
        }
        return result;
    }


}
