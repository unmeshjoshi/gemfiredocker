package com.demobank.gemfire.repository;

import com.demobank.gemfire.functions.Page;
import com.demobank.gemfire.functions.TransactionFilterCriteria;
import com.demobank.gemfire.models.Transaction;
import com.demobank.gemfire.models.TransactionKey;

import java.util.List;

public interface TransactionCache<T> {
    void add(TransactionKey key, List<Transaction> transactions);

    List<Page<T>> getTransactions(TransactionFilterCriteria criteria);

    void clear();
}
