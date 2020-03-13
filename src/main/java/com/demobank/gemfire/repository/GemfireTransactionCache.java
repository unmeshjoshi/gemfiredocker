package com.demobank.gemfire.repository;

import com.demobank.gemfire.functions.Page;
import com.demobank.gemfire.functions.TransactionFilterCriteria;
import com.demobank.gemfire.functions.TransactionsFunction;
import com.demobank.gemfire.models.Transaction;
import com.demobank.gemfire.models.TransactionKey;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Execution;
import org.apache.geode.cache.execute.FunctionService;
import org.apache.geode.pdx.internal.PdxInstanceImpl;

import java.util.ArrayList;
import java.util.List;

public class GemfireTransactionCache implements TransactionCache<PdxInstanceImpl> {
    private final Region transactionRegion;
    private GemFireCache clientCache;

    public GemfireTransactionCache(GemFireCache clientCache) {
        transactionRegion = clientCache.getRegion("Transactions");
        this.clientCache = clientCache;
    }

    @Override
    public void add(TransactionKey key, List<Transaction> transactions) {
        transactionRegion.put(key.toString(), transactions);
    }


    @Override
    public List<Page<PdxInstanceImpl>> getTransactions(TransactionFilterCriteria criteria) {
        List result = executeFunction(criteria);
        return collectPagesFromServers(result);
    }

    private List<Page<PdxInstanceImpl>> collectPagesFromServers(List result) {
        List<Page<PdxInstanceImpl>> mergedResult = new ArrayList<>();
        for (Object o : result) {
            mergedResult.add(getPageInstance(o));
        }
        return mergedResult;
    }

    private List executeFunction(TransactionFilterCriteria criteria) {
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

    @Override
    public void clear() {
        transactionRegion.clear();
    }
}
