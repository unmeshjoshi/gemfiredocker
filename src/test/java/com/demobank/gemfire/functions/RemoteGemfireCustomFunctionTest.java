package com.demobank.gemfire.functions;

import com.demobank.gemfire.repository.PositionCacheImpl;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class RemoteGemfireCustomFunctionTest {
    @Test
    public void shouldCallCustomFunctionOnGenfire() {
        assertEquals(100, new PositionCacheImpl(ClientCacheProvider.instance).multiplyOnServer(10, 10));
    }
}
