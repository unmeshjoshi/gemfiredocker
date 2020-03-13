package com.demobank.gemfire.functions;

import com.demobank.gemfire.models.TransactionKey;

import java.util.ArrayList;
import java.util.List;

public class TransactionFilterCriteria<T> {
    private List<String> accountIds;
    private List<String> dates;
    private String transactionType;
    private int recordsPerPage = 100;
    private int requestedPage;
    private TransactionSortField sortByField = TransactionSortField.AMOUNT;
    private SortOrder sortOrder = SortOrder.ASCENDING;
    //This is always going to be Java object and not PdxInstance for remote invocation.
    // Can not mix Java types and Pdx Instances. For embedded cache tests, it can be PdxInstance, as it is not serialized to servers.

    T lastRecord;


    public TransactionFilterCriteria(){}

    public TransactionFilterCriteria(List<String> accountIds, List<String> dates, int requestedPage) {
        this.accountIds = accountIds;
        this.dates = dates;
        this.requestedPage = requestedPage;
    }

    public TransactionFilterCriteria withRecordsPerPage(int recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
        return this;
    }

    public TransactionFilterCriteria withTransactionType(String transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public TransactionFilterCriteria withLastRecord(T lastRecord) {
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

    public TransactionSortField getSortByField() {
        return sortByField;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
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

    public T getLastRecord() {
        return lastRecord;
    }

    public TransactionFilterCriteria nextPage(T lastRecord) {
        return new TransactionFilterCriteria(accountIds, dates, requestedPage + 1).withRecordsPerPage(recordsPerPage)
                .withLastRecord(lastRecord);
    }
}
