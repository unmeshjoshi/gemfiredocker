package com.demobank.gemfire.functions;

import com.demobank.gemfire.models.TransactionKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TransactionSearchCriteria implements Serializable {
    private List<String> accountIds;
    private List<String> dates;
    private String transactionType;
    private int recordsPerPage = 2;
    private int page;

    private TransactionSearchCriteria(){}

    public TransactionSearchCriteria(List<String> accountIds, List<String> dates, int page) {
        this.accountIds = accountIds;
        this.dates = dates;
        this.page = page;
    }

    public TransactionSearchCriteria withTransactionType(String transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public List<String> getAccountIds() {
        return accountIds;
    }

    public List<String> getDates() {
        return dates;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public int getRecordsPerPage() {
        return recordsPerPage;
    }

    public int getPage() {
        return page;
    }

    public List<String> getKeys() {
        List<String> accountIds = getAccountIds();

        List<String> keys = new ArrayList<>();
        for (String accountId : accountIds) {
            keys.addAll(buildKeys(accountId, getDates()));
        }
        return keys;
    }

    private List<String> buildKeys(String accountId, List<String> dates) {
        List<String> keys = new ArrayList<>();
        for (String date : dates) {
            keys.add(new TransactionKey(accountId, date).toString());
        }
        return keys;
    }
}
