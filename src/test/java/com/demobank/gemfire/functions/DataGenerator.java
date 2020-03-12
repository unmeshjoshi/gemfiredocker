package com.demobank.gemfire.functions;

import com.demobank.gemfire.models.Position;
import com.demobank.gemfire.models.PositionType;
import com.demobank.gemfire.models.Transaction;
import com.demobank.gemfire.models.TransactionKey;
import com.demobank.gemfire.repository.PositionCache;
import com.demobank.gemfire.repository.TransactionCache;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DataGenerator {

    public void seedPositions(PositionCache positionCache) {
        for (int i = 0; i < 100; i++) {
            positionCache.add(new Position(i, PositionType.SAVING, "9952388706", "EQUITY", "CASH_EQUIVALANT", "92824", 4879, "444", new java.math.BigDecimal(130134482), "INR", "2018-01-28"));
            positionCache.add(new Position(i, PositionType.SAVING, "9952388707", "EQUITY_PLUS", "CASH_EQUIVALANT", "92824", 4879, "444", new java.math.BigDecimal(130134482), "INR", "2018-01-28"));
            positionCache.add(new Position(i, PositionType.SAVING, "8805342674", "EQUITY", "CASH_EQUIVALANT", "77189", 9387, "666", new java.math.BigDecimal(362750915), "USD", "2018-01-28"));
            positionCache.add(new Position(i, PositionType.SAVING, "7076923837", "CASH", "CASH_EQUIVALANT", "40718", 9454, "333", new java.math.BigDecimal(780128540), "CAD", "2018-01-28"));
            positionCache.add(new Position(i, PositionType.SAVING, "6334231406", "EQUITY", "CASH_EQUIVALANT", "10120", 2655, "222", new java.math.BigDecimal(837344728), "INR", "2018-01-28"));
            positionCache.add(new Position(i, PositionType.SAVING, "9928894277", "EQUITY", "INVESTMENT", "26510", 9439, "555", new java.math.BigDecimal(6710203), "INR", "2018-01-28"));
        }
    }

    public void seedTransactions(TransactionCache transactionCache, String transactionDate, String accountNumber) {
        TransactionKey key = new TransactionKey(accountNumber, transactionDate);
        transactionCache.add(key, newTransactionsEntry(accountNumber, transactionDate));
    }

    private long tranKey = 0l;
    private List<Transaction> newTransactionsEntry(String accountNumber, String transactionDate) {
        List<Transaction> transactions = new java.util.ArrayList<Transaction>();
        for (int i = 0; i < 100; i++) {
            UUID tranasctionId = UUID.randomUUID();
            BigInteger randomAmount = BigInteger.valueOf(new Random().nextInt(1000000));
            transactions.add(new Transaction(tranasctionId.toString(), tranKey++, transactionDate, randomAmount, "Taxes", accountNumber));
        }
        return transactions;
    }

}
