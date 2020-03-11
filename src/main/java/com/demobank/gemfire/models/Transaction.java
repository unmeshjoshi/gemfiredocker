package com.demobank.gemfire.models;

public class Transaction {

    private Long transactionId;
    private String date;
    private String amount;
    private String type;
    private String accountNumber;

    public Transaction() {
    }

    public Transaction(Long transactionId, String date, String amount, String type, String accountNumber) {
        this.transactionId = transactionId;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.accountNumber = accountNumber;
    }

    public Long getTransactionId() {
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

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", date='" + date + '\'' +
                ", amount='" + amount + '\'' +
                ", type='" + type + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                '}';
    }
}