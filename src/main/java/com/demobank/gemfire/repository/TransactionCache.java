package com.demobank.gemfire.repository;

import com.demobank.gemfire.functions.Page;
import com.demobank.gemfire.functions.TransactionSearchCriteria;
import com.demobank.gemfire.functions.TransactionsFunction;
import com.demobank.gemfire.models.Transaction;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Execution;
import org.apache.geode.cache.execute.FunctionService;
import org.apache.geode.pdx.internal.PdxInstanceImpl;

import java.util.ArrayList;
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


    public List<Page> getTransactions(TransactionSearchCriteria criteria) {
        List result = executeFunction(criteria);
        return collectPagesFromServers(result);
    }

    private List<Page> collectPagesFromServers(List result) {
        List<Page> mergedResult = new ArrayList<>();
        for (Object o : result) {
            mergedResult.add(getPageInstance(o));
        }
        return mergedResult;
    }

    private List executeFunction(TransactionSearchCriteria criteria) {
        TransactionsFunction function = new TransactionsFunction();
        Execution execution = FunctionService.onRegion(transactionRegion).withArgs(criteria);
        return (List) execution.execute(function).getResult();
    }

    private Page getPageInstance(Object o) {
        if (o instanceof Page) { //In case of embedded server
            return (Page) o;

        } else if (o instanceof PdxInstanceImpl) { //In case of remote server.
            PdxInstanceImpl pageInstace = (PdxInstanceImpl) o;
            return pageFromPdxInstance(pageInstace);
        }
        throw new IllegalArgumentException("Unknown type for page " + o);
    }

    private Page pageFromPdxInstance(PdxInstanceImpl pageInstace) {
        return new Page((Integer) pageInstace.getField("pageNumber"),
                        (List) pageInstace.getField("results"),
                        (Integer) pageInstace.getField("totalNumberOfPages"));
    }

    public void clear() {
        transactionRegion.clear();
    }
}
