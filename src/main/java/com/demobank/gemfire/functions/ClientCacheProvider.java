package com.demobank.gemfire.functions;

import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;

public class ClientCacheProvider {
    public static GemFireCache instance = createClientCache();
    private static GemFireCache createClientCache() {
        ClientCache clientCache = new ClientCacheFactory().addPoolLocator("172.17.0.2", 9009)
                .setPdxSerializer(new ReflectionBasedAutoSerializer("com.gemfire.functions.*"))
                .setPoolMinConnections(50)
                .create();

        clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY).create("Positions");
        clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY).create("FxRates");
        clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY).create("MarketPrices");
        return clientCache;
    }

    private ClientCacheProvider() {}
}
