package com.demobank.gemfire.functions;

import java.io.Serializable;

public class TransactionSearchCriteria implements Serializable {
    private String transactionType;
    private String accountId;
    private String date;
    private int recordsPerPage = 2;
    private int page;

    public TransactionSearchCriteria(){}

    public TransactionSearchCriteria(String accountId, String date, int page) {
        this.accountId = accountId;
        this.date = date;
        this.page = page;
    }

    public TransactionSearchCriteria withTransactionType(String transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getDate() {
        return date;
    }

    public int getRecordsPerPage() {
        return recordsPerPage;
    }

    public int getPage() {
        return page;
    }
}
