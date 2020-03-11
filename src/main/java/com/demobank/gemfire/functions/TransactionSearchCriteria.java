package com.demobank.gemfire.functions;

import com.demobank.gemfire.models.TransactionKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionSearchCriteria implements Serializable {
    private List<String> accountIds;
    private List<String> dates;
    private String transactionType;
    private int recordsPerPage = 100;
    private int requestedPage;
    Optional<Object> lastRecord;

    private TransactionSearchCriteria(){}

    public TransactionSearchCriteria(List<String> accountIds, List<String> dates, int requestedPage) {
        this.accountIds = accountIds;
        this.dates = dates;
        this.requestedPage = requestedPage;
    }

    public TransactionSearchCriteria withRecordsPerPage(int recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
        return this;
    }

    public TransactionSearchCriteria withTransactionType(String transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public TransactionSearchCriteria withLastRecord(Optional<Object> lastRecord) {
        this.lastRecord = lastRecord;
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

    public int getRequestedPage() {
        return requestedPage;
    }

    public List<TransactionKey> getKeys() {
        List<String> accountIds = getAccountIds();

        List<TransactionKey> keys = new ArrayList<>();
        for (String accountId : accountIds) {
            keys.addAll(buildKeys(accountId, getDates()));
        }
        return keys;
    }

    private List<TransactionKey> buildKeys(String accountId, List<String> dates) {
        List<TransactionKey> keys = new ArrayList<>();
        for (String date : dates) {
            keys.add(new TransactionKey(accountId, date));
        }
        return keys;
    }

    public Optional<Object> getLastRecord() {
        return lastRecord;
    }

    public TransactionSearchCriteria nextPage(Optional<Object> lastRecord) {
        return new TransactionSearchCriteria(accountIds, dates, requestedPage + 1).withRecordsPerPage(recordsPerPage)
                .withLastRecord(lastRecord);
    }
}
