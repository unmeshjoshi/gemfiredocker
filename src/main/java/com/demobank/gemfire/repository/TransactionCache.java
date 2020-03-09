package com.demobank.gemfire.repository;

import com.demobank.gemfire.functions.TransactionSearchCriteria;
import com.demobank.gemfire.functions.TransactionsFunction;
import com.demobank.gemfire.models.Transaction;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Execution;
import org.apache.geode.cache.execute.FunctionService;
import org.apache.geode.pdx.PdxInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TransactionCache {
    private final Region transactionRegion;
    private GemFireCache clientCache;

    public TransactionCache(GemFireCache clientCache) {
        transactionRegion = clientCache.getRegion("Transactions");
        this.clientCache = clientCache;
    }

    public void add(String key, List<Transaction> transactions) {
        transactionRegion.put(key, transactions);
    }

    public List<PdxInstance> getTransactions(TransactionSearchCriteria criteria) {
        TransactionsFunction function = new TransactionsFunction();
        Execution execution = FunctionService.onRegion(transactionRegion).withArgs(criteria);
        List result = (List) execution.execute(function).getResult();
        List<PdxInstance> mergedResult = new ArrayList<>();
        for (Object o : result) {
            mergedResult.addAll((Collection<? extends PdxInstance>) o);
        }
        return mergedResult;
    }

    public void clear() {
        transactionRegion.clear();
    }
}
