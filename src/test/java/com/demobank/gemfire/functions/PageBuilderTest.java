package com.demobank.gemfire.functions;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class PageBuilderTest {

    @Test
    public void shouldGiveTotalNumberOfPages() {
        assertEquals(3, PageBuilder.getTotalNumberOfPages(6, 2));
        assertEquals(1, PageBuilder.getTotalNumberOfPages(1, 2));
        assertEquals(4, PageBuilder.getTotalNumberOfPages(7, 2));
        assertEquals(1, PageBuilder.getTotalNumberOfPages(7, 7));
    }

    @Test
    public void shouldGivenRequestedPage() {
        assertEquals(new PageBuilder(2, Arrays.asList(1, 2, 3, 4, 5, 6)).getPage(2).getResults(), Arrays.asList(3, 4));
        assertEquals(new PageBuilder(2, Arrays.asList(1)).getPage(1).getResults(), Arrays.asList(1));
        assertEquals(new PageBuilder(2, Arrays.asList(1, 2, 3, 4, 5, 6, 7)).getPage(4).getResults(), Arrays.asList(7));
    }

    @Test
    public void shouldReturnEmptyFirtPageIfNoRecords() {
        PageBuilder pageBuilder = new PageBuilder(1, Collections.EMPTY_LIST);
        assertEquals(pageBuilder.getPage(2).getResults(), Collections.EMPTY_LIST);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldValidateInvalidPageRequest() {
        assertEquals(new PageBuilder(4, Arrays.asList(1, 2, 3, 4, 5, 6, 7)).getPage(4).getResults(), Arrays.asList(7));
    }

}