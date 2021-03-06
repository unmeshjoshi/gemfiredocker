package com.demobank.gemfire.functions;

import java.util.List;

public class Page<T> {
    int totalNumberOfPages;
    int pageNumber;
    private List<T> results;

    public Page(){}

    public Page(int pageNumber, List<T> results, int totalNumberOfPages) {
        this.pageNumber = pageNumber;
        this.results = results;
        this.totalNumberOfPages = totalNumberOfPages;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public List<T> getResults() {
        return results;
    }

    public int getTotalNumberOfPages() {
        return totalNumberOfPages;
    }

    public T getLastRecord() {
        if (!results.isEmpty()) {
            return results.get(results.size() - 1);
        }
        return null;
    }

    public T firstRecord() {
        return results.get(0);
    }

    public T lastRecord() {
        return results.get(results.size() - 1);
    }
}
