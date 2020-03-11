package com.demobank.gemfire.models;

import java.util.Objects;

public class TransactionKey {
    String accountNumber;
    String transactionDate;
    public TransactionKey(String accountNumber, String transactionDate) {
        this.accountNumber = accountNumber;
        this.transactionDate = transactionDate;
    }

    public String toString() {
        return accountNumber + "_" + transactionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionKey that = (TransactionKey) o;
        return Objects.equals(accountNumber, that.accountNumber) &&
                Objects.equals(transactionDate, that.transactionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, transactionDate);
    }
}
