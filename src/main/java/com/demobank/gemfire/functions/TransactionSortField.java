package com.demobank.gemfire.functions;

import com.demobank.gemfire.models.Transaction;
import org.apache.geode.pdx.PdxInstance;

import java.math.BigInteger;
import java.util.Comparator;

enum SortOrder {
    DESCENDING, ASCENDING
}

public enum TransactionSortField {
    AMOUNT((Object t1, Object t2) -> {
        int compareResult = getAmount(t1).compareTo(getAmount(t2));
        if (compareResult != 0) {
            return compareResult;
        }
        return getTranKey(t1).compareTo(getTranKey(t2));
    });

    public Comparator<Object> getComparator() {
        return comparator;
    }

    private static BigInteger getAmount(Object obj) {
        if (obj instanceof Transaction) {
            return ((Transaction)obj).getAmount();
        } else if (obj instanceof PdxInstance) {
            return (BigInteger)((PdxInstance)obj).getField("amount");
        }
        throw new IllegalArgumentException("Unknown type for " + obj);
    }

    private static Long getTranKey(Object obj) {
        if (obj instanceof Transaction) {
            return ((Transaction)obj).getTranKey();
        } else if (obj instanceof PdxInstance) {
            return (Long)((PdxInstance)obj).getField("tranKey");
        }
        throw new IllegalArgumentException("Unknown type for " + obj);
    }

    private Comparator<Object> comparator;
    TransactionSortField(Comparator<Object> comparator) {
        this.comparator = comparator;
    }
}
