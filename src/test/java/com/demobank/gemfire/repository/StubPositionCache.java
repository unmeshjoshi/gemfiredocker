package com.demobank.gemfire.repository;

import com.demobank.gemfire.models.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StubPositionCache implements PositionCache {
    Map<String, List<Position>> positionMap = new HashMap<>();



    @Override
    public void add(String acctKey, List<Position> positions) {
        positionMap.put(acctKey, positions);
    }
}
