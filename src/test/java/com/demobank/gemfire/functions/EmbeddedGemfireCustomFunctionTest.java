package com.demobank.gemfire.functions;

import com.demobank.gemfire.repository.GemfireTransactionCache;
import com.demobank.gemfire.repository.PositionCacheImpl;
import com.demobank.gemfire.repository.TransactionCache;
import org.apache.geode.cache.Cache;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class EmbeddedGemfireCustomFunctionTest extends BaseGemfireTest {
    PositionCacheImpl positionCache;
    private TransactionCache transactionCache;

    @Before
    public void seedData() {
        Cache cache = createCache();
        positionCache = new PositionCacheImpl(cache);
        transactionCache = new GemfireTransactionCache(cache);
        new DataGenerator().seedPositions("acctKey", positionCache);
    }

    @Test
    public void executeBasicFunction() {
        assertEquals(100, positionCache.multiplyOnServer(10, 10));
    }
}
