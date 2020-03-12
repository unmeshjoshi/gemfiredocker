package com.demobank.gemfire.models;

import java.math.BigInteger;

public class Transaction {

    private String transactionId;
    private Long tranKey;
    private String date;
    private BigInteger amount;
    private String type;
    private String accountNumber;

    public Transaction() {
    }

    public Transaction(String transactionId, Long tranKey, String date, BigInteger amount, String type, String accountNumber) {
        this.transactionId = transactionId;
        this.tranKey = tranKey;
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

    public BigInteger getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Long getTranKey() {
        return tranKey;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", tranKey=" + tranKey +
                ", date='" + date + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                '}';
    }
}