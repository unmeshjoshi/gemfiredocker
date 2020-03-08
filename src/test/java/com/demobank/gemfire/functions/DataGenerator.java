package com.demobank.gemfire.functions;

import com.demobank.gemfire.models.Position;
import com.demobank.gemfire.models.PositionType;
import com.demobank.gemfire.repository.PositionCache;

public class DataGenerator {
    private PositionCache positionCache;

    public DataGenerator(PositionCache positionCache) {
        this.positionCache = positionCache;
    }

    public void seedPositions() {
        for (int i = 0; i < 100; i++) {
            positionCache.add(new Position(i, PositionType.SAVING, "9952388706", "EQUITY", "CASH_EQUIVALANT", "92824", 4879, "444", new java.math.BigDecimal(130134482), "INR", "2018-01-28"));
            positionCache.add(new Position(i, PositionType.SAVING, "9952388707", "EQUITY_PLUS", "CASH_EQUIVALANT", "92824", 4879, "444", new java.math.BigDecimal(130134482), "INR", "2018-01-28"));
            positionCache.add(new Position(i, PositionType.SAVING, "8805342674", "EQUITY", "CASH_EQUIVALANT", "77189", 9387, "666", new java.math.BigDecimal(362750915), "USD", "2018-01-28"));
            positionCache.add(new Position(i, PositionType.SAVING, "7076923837", "CASH", "CASH_EQUIVALANT", "40718", 9454, "333", new java.math.BigDecimal(780128540), "CAD", "2018-01-28"));
            positionCache.add(new Position(i, PositionType.SAVING, "6334231406", "EQUITY", "CASH_EQUIVALANT", "10120", 2655, "222", new java.math.BigDecimal(837344728), "INR", "2018-01-28"));
            positionCache.add(new Position(i, PositionType.SAVING, "9928894277", "EQUITY", "INVESTMENT", "26510", 9439, "555", new java.math.BigDecimal(6710203), "INR", "2018-01-28"));
        }
    }
}
