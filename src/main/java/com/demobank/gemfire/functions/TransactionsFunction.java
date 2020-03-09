package com.demobank.gemfire.functions;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.geode.cache.execute.ResultSender;
import org.apache.geode.cache.partition.PartitionRegionHelper;
import org.apache.geode.internal.logging.LogService;
import org.apache.geode.pdx.PdxInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TransactionsFunction  implements Function {
    @Override
    public boolean hasResult() {
        return true;
    }

    @Override
    public void execute(FunctionContext context) {
        RegionFunctionContext rctx = (RegionFunctionContext) context;
        Region<String, List<PdxInstance>> dataSet = rctx.getDataSet();

        Region<String, List<PdxInstance>> localData = PartitionRegionHelper.getLocalData(dataSet);

        TransactionSearchCriteria criteria = (TransactionSearchCriteria)context.getArguments();
        List<String> accountIds = criteria.getAccountIds();

        List<String> keys = new ArrayList<>();
        for (String accountId : accountIds) {
            keys.addAll(buildKeys(accountId, criteria.getDates()));
        }

        Map<String, List<PdxInstance>> all = localData.getAll(keys);

        List<PdxInstance> allTransactions = new ArrayList<>();
        Collection<List<PdxInstance>> values = all.values();
        for (List<PdxInstance> value : values) {
            if (value != null) {
                allTransactions.addAll(value);
            }
        }

        LogService.getLogger().info("Function returning result " + this.getClass() + " loaded from " + this.getClass().getClassLoader());

        ResultSender resultSender = rctx.getResultSender();
        resultSender.lastResult(allTransactions);
    }

    private List<String> buildKeys(String accountId, List<String> dates) {
        List<String> keys = new ArrayList<>();
        for (String date : dates) {
            keys.add(accountId + "_" + date);
        }
        return keys;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    @Override
    public boolean optimizeForWrite() {
        return false;
    }

    @Override
    public boolean isHA() {
        return false;
    }
}
