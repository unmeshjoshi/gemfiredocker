package com.demobank.gemfire.functions;

import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;

public class ClientCacheProvider {
    public static GemFireCache instance = createClientCache();
    private static GemFireCache createClientCache() {
        ClientCache clientCache = new ClientCacheFactory().addPoolLocator("locator1", 9009)
                .setPdxReadSerialized(true)
                .setPdxSerializer(new ReflectionBasedAutoSerializer("com.demobank.gemfire.models.*,com.gemfire.demobank.functions.*"))
                .setPoolMinConnections(1)
                .create();

        clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY).create("Positions");
        clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY).create("Transactions");
        clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY).create("FxRates");
        clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY).create("MarketPrices");
        return clientCache;
    }

    private ClientCacheProvider() {}
}
