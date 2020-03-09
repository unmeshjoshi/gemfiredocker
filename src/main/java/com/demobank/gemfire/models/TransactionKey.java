package com.demobank.gemfire.models;

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
}
