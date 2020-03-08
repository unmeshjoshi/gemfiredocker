package com.demobank.gemfire.functions;

import com.demobank.gemfire.repository.PositionCache;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class RemoteGemfireCustomFunctionTest {
    @Test
    public void shouldCallCustomFunctionOnGenfire() {
        assertEquals(100, new PositionCache(ClientCacheProvider.instance).multiplyOnServer(10, 10));
    }
}
