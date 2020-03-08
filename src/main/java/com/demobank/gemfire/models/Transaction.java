package com.demobank.gemfire.models;

public class Transaction {

    private String transactionId;
    private String date;
    private String amount;
    private String type;
    private String accountNumber;

    public Transaction() {
    }

    public Transaction(String transactionId, String date, String amount, String type, String accountNumber) {
        this.transactionId = transactionId;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.accountNumber = accountNumber;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}