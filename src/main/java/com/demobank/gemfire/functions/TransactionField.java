package com.demobank.gemfire.functions;

import com.demobank.gemfire.models.Transaction;

import java.util.Comparator;

public enum TransactionField {
    AMOUNT((Transaction t1, Transaction t2) -> {
        int compareResult = t2.getAmount().compareTo(t1.getAmount());
        if (compareResult != 0) {
            return compareResult;
        }
        return t2.getTranKey().compareTo(t1.getTranKey());
    });

    public Comparator<Transaction> getComparator() {
        return comparator;
    }

    private Comparator<Transaction> comparator;
    TransactionField(Comparator<Transaction> comparator) {
        this.comparator = comparator;
    }
}
