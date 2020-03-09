package com.demobank.gemfire.functions;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.geode.cache.execute.ResultSender;
import org.apache.geode.cache.partition.PartitionRegionHelper;
import org.apache.geode.internal.logging.LogService;
import org.apache.geode.pdx.PdxInstance;

import java.util.List;

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
        String key = criteria.getAccountId() + "_" + criteria.getDate();

        List<PdxInstance> transactions  = localData.get(key);

        LogService.getLogger().info("Function returning result " + this.getClass() + " loaded from " + this.getClass().getClassLoader());

        ResultSender resultSender = rctx.getResultSender();
        resultSender.lastResult(transactions);
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
