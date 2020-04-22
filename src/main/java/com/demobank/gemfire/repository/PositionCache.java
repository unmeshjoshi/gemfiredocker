package com.demobank.gemfire.repository;

import com.demobank.gemfire.models.Position;

import java.util.List;

public interface PositionCache<T> {
    public void add(String acctKey, List<T> positions);
}
