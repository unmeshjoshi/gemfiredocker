package com.demobank.gemfire.functions;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.RegionShortcut;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;

import java.util.Properties;

import static org.apache.geode.distributed.ConfigurationProperties.MCAST_PORT;

public class BaseGemfireTest {

    public Cache createCache() {
            Properties props = new Properties();
            props.setProperty(MCAST_PORT, "0");

            CacheFactory factory = new CacheFactory(props);
            Cache cache = factory
                    .setPdxSerializer(new ReflectionBasedAutoSerializer("com.demobank.gemfire.functions.*", "com.demobank.gemfire.models.*"))
                    .setPdxReadSerialized(true)
                    .setPdxDiskStore("DEFAULT")
                    .create();

            cache.createRegionFactory(RegionShortcut.PARTITION).create("Positions");
            cache.createRegionFactory(RegionShortcut.PARTITION).create("Transactions");
            cache.createRegionFactory(RegionShortcut.REPLICATE).create("FxRates");
            cache.createRegionFactory(RegionShortcut.REPLICATE).create("MarketPrices");
            cache.createRegionFactory(RegionShortcut.PARTITION).create("Visibility");

            return cache;
    }
}
