package com.demobank.gemfire.functions;

import com.demobank.gemfire.repository.PositionCache;
import org.apache.geode.cache.Cache;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class EmbeddedGemfireCustomFunctionTest extends BaseGemfireTest {
    PositionCache positionCache;
    @Before
    public void seedData() {
        Cache cache = createCache();
        positionCache = new PositionCache(cache);
        new DataGenerator(positionCache).seedPositions();
    }

    @Test
    public void getTransactions() {
        assertEquals(100, positionCache.multiplyOnServer(10, 10));
    }
}
