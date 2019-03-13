package com.gemfire.functions;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class GemfireCustomFunctionTest {
    @Test
    public void shouldCallCustomFunctionOnGenfire() {
        assertEquals(20, new PositionCache(ClientCacheProvider.instance).multiplyOnServer(10, 10));
    }
}
