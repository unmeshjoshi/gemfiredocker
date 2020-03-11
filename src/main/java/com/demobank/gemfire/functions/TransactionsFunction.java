package com.demobank.gemfire.functions;

import com.demobank.gemfire.models.TransactionKey;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.geode.cache.partition.PartitionRegionHelper;
import org.apache.geode.internal.logging.LogService;
import org.apache.geode.pdx.PdxInstance;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionsFunction  implements Function {
    @Override
    public boolean hasResult() {
        return true;
    }

    @Override
    public void execute(FunctionContext context) {
        RegionFunctionContext rctx = (RegionFunctionContext) context;
        TransactionSearchCriteria searchCriteria = (TransactionSearchCriteria) context.getArguments();
        List<TransactionKey> keys = searchCriteria.getKeys();

        Region<String, List<PdxInstance>> localData = getLocalData(rctx);
        List<PdxInstance> allTransactions = getTransactionsFor(localData, keys);

        LogService.getLogger().info("Function returning result " + this.getClass() + " loaded from " + this.getClass().getClassLoader());
        Page page = new PageBuilder(searchCriteria.getRecordsPerPage(), allTransactions).getPage(searchCriteria.getRequestedPage());
        sendResult(rctx, page);
    }

    private void sendResult(RegionFunctionContext rctx, Page page) {
        LogService.getLogger().info("Returning page " + page.getResults());
        rctx.getResultSender().lastResult(page);
    }

    private List<PdxInstance> getTransactionsFor(Region<String, List<PdxInstance>> localData, List<TransactionKey> keys) {
        List<String> keyStrings = keys.stream().map(key -> key.toString()).collect(Collectors.toList());
        Map<String, List<PdxInstance>> all = localData.getAll(keyStrings);
        return filterNulls(all.values()).flatMap(x -> x.stream()).collect(Collectors.toList());
        //filter null values for some keys.
    }

    private Stream<List<PdxInstance>> filterNulls(Collection<List<PdxInstance>> values) {
        return values.stream().filter(x -> x != null);
    }

    private Region<String, List<PdxInstance>> getLocalData(RegionFunctionContext rctx) {
        return PartitionRegionHelper.getLocalData(rctx.getDataSet());
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
