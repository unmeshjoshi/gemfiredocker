package com.demobank.gemfire.repository;

import com.demobank.gemfire.functions.MultArgs;
import com.demobank.gemfire.functions.Multiply;
import com.demobank.gemfire.models.Position;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Execution;
import org.apache.geode.cache.execute.FunctionService;
import org.apache.geode.cache.execute.ResultCollector;

import java.util.List;

public class PositionCacheImpl implements PositionCache<Position> {

    private final Region positionRegion;
    private GemFireCache clientCache;

    public PositionCacheImpl(GemFireCache clientCache) {
        positionRegion = clientCache.getRegion("Positions");
        this.clientCache = clientCache;
    }

    public int multiplyOnServer(int x, int y) {
        Multiply function = new Multiply();
        Execution execution = FunctionService.onRegion(positionRegion).withArgs(new MultArgs(x, y));
        ResultCollector result = execution.execute(function); //need to execute with id to execute function remotely on the server
        return (Integer) ((List) result.getResult()).get(0);
    }

    public void clear() {
        positionRegion.clear();
    }

    @Override
    public void add(String acctKey, List<Position> positions) {
        positionRegion.put(acctKey, positions);
    }
}


