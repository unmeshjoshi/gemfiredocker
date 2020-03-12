package com.demobank.gemfire.functions;

import java.util.ArrayList;
import java.util.List;

public class PageBuilder {
    private final int recordsPerPage;
    private final List allRecords;

    public PageBuilder(int recordsPerPage, List allRecords) {
        this.recordsPerPage = recordsPerPage;
        this.allRecords = allRecords;
    }

    public Page getPage(int requestedPage) {
        int totalNumberOfPages = getTotalNumberOfPages();
        if (allRecords.size() == 0 && requestedPage == 1) {
            return new Page(1, allRecords, 1);
        }
        if(recordsPerPage <= 0 || requestedPage <= 0 || requestedPage > totalNumberOfPages) {
            throw new IllegalArgumentException("Invalid requested page : " + recordsPerPage + " Total pages=" + totalNumberOfPages);
        }

        int pageStartIndex = (requestedPage - 1) * recordsPerPage;
        return getPageStartingAt(requestedPage, pageStartIndex);
    }

    public Page getPageStartingAt(int requestedPage, int pageStartIndex) {
        int pageEndIndex = pageStartIndex + recordsPerPage;
        int totalNumberOfRecords = allRecords.size();

        List recordsForPage = new ArrayList();
        int endIndex = Math.min(pageEndIndex, totalNumberOfRecords);
        //create a new list to make sure it serializes
        for (int i = pageStartIndex; i < endIndex; i++) {
            recordsForPage.add(allRecords.get(i));
        }

        return new Page(requestedPage, recordsForPage, -1);
    }

    public int getTotalNumberOfPages() {
        return getTotalNumberOfPages(allRecords.size(), recordsPerPage);
    }

    public Page firstPage() {
        return getPage(1);
    }

    //TBD. move to utility
    static int getTotalNumberOfPages(int totalNumberOfRecords, int recordsPerPage) {
        return (totalNumberOfRecords / recordsPerPage) + ((totalNumberOfRecords % recordsPerPage) == 0? 0:1);
    }

}
