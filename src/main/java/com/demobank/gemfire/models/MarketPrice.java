package com.demobank.gemfire.models;

import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxSerializable;
import org.apache.geode.pdx.PdxWriter;

import java.math.BigDecimal;

public class MarketPrice implements PdxSerializable {
    String symbol;
    BigDecimal openingPrice;
    BigDecimal closingPrice;
    BigDecimal high;
    BigDecimal low;

    public MarketPrice() {
    }

    public MarketPrice(String symbol, BigDecimal openingPrice, BigDecimal closingPrice, BigDecimal high, BigDecimal low) {
        this.openingPrice = openingPrice;
        this.closingPrice = closingPrice;
        this.high = high;
        this.low = low;
    }

    public BigDecimal getOpeningPrice() {
        return openingPrice;
    }

    public BigDecimal getClosingPrice() {
        return closingPrice;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public String getSymbol() {
        return symbol;
    }

    public String key() {
        return symbol;
    }

    @Override
    public void toData(PdxWriter writer) {
        writer.writeString("symbol", this.symbol);
        writer.writeObject("openingPrice", this.openingPrice);
        writer.writeObject("closingPrice", this.closingPrice);
        writer.writeObject("high", this.high);
        writer.writeObject("low", this.low);
    }

    @Override
    public void fromData(PdxReader reader) {

    }
}
