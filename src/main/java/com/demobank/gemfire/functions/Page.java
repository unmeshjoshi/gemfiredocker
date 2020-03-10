package com.demobank.gemfire.functions;

import java.util.List;

public class Page {
    int totalNumberOfPages;
    int pageNumber;
    List<?> results;

    public Page(){}

    public Page(int pageNumber, List<?> results, int totalNumberOfPages) {
        this.pageNumber = pageNumber;
        this.results = results;
        this.totalNumberOfPages = totalNumberOfPages;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public List<?> getResults() {
        return results;
    }

    public int getTotalNumberOfPages() {
        return totalNumberOfPages;
    }
}
