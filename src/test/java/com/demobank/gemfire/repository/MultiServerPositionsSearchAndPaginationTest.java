package com.demobank.gemfire.repository;

import com.demobank.gemfire.functions.DataGenerator;
import com.demobank.gemfire.models.Position;

import java.util.Arrays;
import java.util.List;

class StubClientSidePositionCache implements PositionCache<Position>  {
    List<StubPositionCache> servers;

    public StubClientSidePositionCache(List<StubPositionCache> servers) {
        this.servers = servers;
    }


    public void add(String key, List<Position> transactions) {
        //noop
    }
}

public class MultiServerPositionsSearchAndPaginationTest {
    StubPositionCache server1 = new StubPositionCache();
    StubPositionCache server2 = new StubPositionCache();


    DataGenerator dataGenerator = new DataGenerator();

    StubClientSidePositionCache clientSidePositionCache = new StubClientSidePositionCache(Arrays.asList(server1, server2));


}
