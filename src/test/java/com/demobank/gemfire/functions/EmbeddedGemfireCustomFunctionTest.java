package com.demobank.gemfire.functions;

import com.demobank.gemfire.repository.PositionCache;
import com.demobank.gemfire.repository.TransactionCache;
import org.apache.geode.cache.Cache;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class EmbeddedGemfireCustomFunctionTest extends BaseGemfireTest {
    PositionCache positionCache;
    private TransactionCache transactionCache;

    @Before
    public void seedData() {
        Cache cache = createCache();
        positionCache = new PositionCache(cache);
        transactionCache = new TransactionCache(cache);
        new DataGenerator(positionCache, transactionCache).seedPositions();
    }

    @Test
    public void executeBasicFunction() {
        assertEquals(100, positionCache.multiplyOnServer(10, 10));
    }
}
