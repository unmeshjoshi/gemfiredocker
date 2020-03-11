package com.demobank.gemfire.repository;

import com.demobank.gemfire.models.Position;

public class StubPositionCache implements PositionCache {
    @Override
    public void add(Position position) {
        //noop for now.
    }
}
