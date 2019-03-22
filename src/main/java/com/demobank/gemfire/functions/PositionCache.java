package com.demobank.gemfire.functions;

import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Execution;
import org.apache.geode.cache.execute.FunctionService;
import org.apache.geode.cache.execute.ResultCollector;

import java.util.List;

public class PositionCache {

    private final Region reg;
    private GemFireCache clientCache;

    public PositionCache(GemFireCache clientCache) {
        reg = clientCache.getRegion("Positions");
        this.clientCache = clientCache;
    }

    public int multiplyOnServer(int x, int y) {
        Multiply function = new Multiply();
        Execution execution = FunctionService.onRegion(reg).withArgs(new MultArgs(x, y));
        ResultCollector result = execution.execute(function.getId());
        return (Integer) ((List) result.getResult()).get(0);
    }
}


