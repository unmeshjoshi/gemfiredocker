package com.demobank.gemfire.functions;

import java.util.Comparator;

/*
 Comparators in TransactionSortField compare always for ascending order. This wrapper is a decorator
 to convert based on sort order passed to TransactionFunction
 This also helps find out immidiate next value to the last record
 */
class SortOrderBasedComparatorDecorator implements Comparator {
    private final SortOrder sortOrder;
    private final Comparator comparator;

    public SortOrderBasedComparatorDecorator(SortOrder sortOrder, Comparator comparator) {
        this.sortOrder = sortOrder;
        this.comparator = comparator;
    }

    @Override
    public int compare(Object o1, Object o2) {
        int result = comparator.compare(o1, o2);
        return sortOrder == SortOrder.ASCENDING ? result : -1 * result;
    }

    public boolean isNext(Object valueToCompare, Object valueFromLastRecordOfPreviousPage) {
        int result = compare(valueToCompare, valueFromLastRecordOfPreviousPage);
        return sortOrder == SortOrder.ASCENDING ? result > 0 : result < 0;
    }
}
