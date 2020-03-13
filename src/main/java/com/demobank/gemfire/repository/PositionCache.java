package com.demobank.gemfire.repository;

import com.demobank.gemfire.models.Position;

import java.util.List;

public interface PositionCache {
    public void add(String acctKey, List<Position> positions);
}
