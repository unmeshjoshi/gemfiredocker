package com.demobank.gemfire.functions;

import com.demobank.gemfire.models.TransactionKey;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.geode.cache.partition.PartitionRegionHelper;
import org.apache.geode.internal.logging.LogService;
import org.apache.geode.pdx.PdxInstance;

import java.util.ArrayList;
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

        //Need to get searchcriteria. In case of embedded tests its Java object and in case of remote tests its PdxInstance.
        SearchCriteriaWrapper searchCriteriaWrapper = new SearchCriteriaWrapper(context);

        TransactionSearchCriteria searchCriteria = searchCriteriaWrapper.getSearchCriteria();
        PdxInstance lastRecord = searchCriteriaWrapper.getLastRecord();

        Region<String, List<PdxInstance>> localData = getLocalData(rctx);
        Page page = getPage(searchCriteria, localData, lastRecord);

        LogService.getLogger().info("Function returning result " + this.getClass() + " loaded from " + this.getClass().getClassLoader());
        sendResult(rctx, page);
    }

    private void sendResult(RegionFunctionContext rctx, Page page) {
        LogService.getLogger().info("Returning page " + page.getResults());
        rctx.getResultSender().lastResult(page);
    }

    public Page getPage(TransactionSearchCriteria criteria, Region<String, List<PdxInstance>> localData, PdxInstance lastRecord) {
        List<PdxInstance> result = getTransactionsFor(localData, criteria.getKeys());
        List<PdxInstance> sortedTransactions = sortTransactions(result, criteria.getSortByField());

        if (criteria.getRequestedPage() == 1) {
            return firstPage(criteria, sortedTransactions);
        }

        return getNextPageFromLastRecord(criteria, sortedTransactions, lastRecord);
    }

    private List<PdxInstance> sortTransactions(List<PdxInstance> result, TransactionSortField sortByField) {
        return result.stream().sorted(sortByField.getComparator()).collect(Collectors.toList());
    }

    private Page getNextPageFromLastRecord(TransactionSearchCriteria criteria, List<PdxInstance> sortedTransactions, PdxInstance lastTransactionFromPreviousPage) {
        if (lastTransactionFromPreviousPage == null) { //always expect last record on pages after first
            throw new IllegalArgumentException("Need last record from previous page for getting pages beyond first page");
        }
        int index = firstIndexMoreThanOrEqual(criteria.getSortByField(), sortedTransactions, lastTransactionFromPreviousPage);
        if (index == -1) { // no more records from this node. Return empty list
            return new Page(criteria.getRequestedPage(), new ArrayList(), -1);
        }
        return new PageBuilder(criteria.getRecordsPerPage(), sortedTransactions).getPageStartingAt(criteria.getRequestedPage(), index);
    }

    private Page firstPage(TransactionSearchCriteria criteria, List<PdxInstance> sortedTransactions) {
        return new PageBuilder(criteria.getRecordsPerPage(), sortedTransactions).getPage(1);
    }

    private int firstIndexMoreThanOrEqual(TransactionSortField sortByField, List<PdxInstance> sortedTransactions, PdxInstance lastTransactionFromPreviousPage) {
        for (int i = 0; i < sortedTransactions.size(); i++) {
            if (sortByField.getComparator().compare(sortedTransactions.get(i), lastTransactionFromPreviousPage) > 0) {
                return i;
            }
        }
        return -1;
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

    /**
     * A utility class to get criteria object from context.
     * Its needed because for embedded gemfire in tests, the criteria object is not Pdx (as it is not serialized over the network)
     * and in case of remote gemfire, it will be pdx instance.
     */
    private class SearchCriteriaWrapper {
        private FunctionContext context;
        private TransactionSearchCriteria searchCriteria;
        private PdxInstance lastRecord;

        public SearchCriteriaWrapper(FunctionContext context) {
            this.context = context;
            Object arguments = this.context.getArguments();
            if (arguments instanceof PdxInstance) {
                PdxInstance criteriaPdxInstance = (PdxInstance) this.context.getArguments();
                lastRecord = (PdxInstance) criteriaPdxInstance.getField("lastRecord"); //pick up last record pdx instance before converting to Java Object
                searchCriteria = (TransactionSearchCriteria) criteriaPdxInstance.getObject();

            } else if (arguments instanceof TransactionSearchCriteria) {
               searchCriteria = (TransactionSearchCriteria) arguments;
               lastRecord = (PdxInstance) searchCriteria.getLastRecord();
            }
        }

        public TransactionSearchCriteria getSearchCriteria() {
            return searchCriteria;
        }

        public PdxInstance getLastRecord() {
            return lastRecord;
        }
    }
}
