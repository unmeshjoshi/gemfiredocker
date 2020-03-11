package com.demobank.gemfire.repository;

import com.demobank.gemfire.functions.Page;
import com.demobank.gemfire.functions.TransactionSearchCriteria;
import com.demobank.gemfire.models.Transaction;
import com.demobank.gemfire.models.TransactionKey;

import java.util.List;

public interface TransactionCache {
    void add(TransactionKey key, List<Transaction> transactions);

    List<Page> getTransactions(TransactionSearchCriteria criteria);

    void clear();
}
