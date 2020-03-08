package com.demobank.gemfire.models;

import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxSerializable;
import org.apache.geode.pdx.PdxWriter;

import java.math.BigDecimal;

public class FxRate implements PdxSerializable {
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal fxRate;
    private String forDate;

    public FxRate() {
    }

    public FxRate(String fromCurrency, String toCurrency, BigDecimal fxRate, String forDate) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.fxRate = fxRate;
        this.forDate = forDate;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public BigDecimal getFxRate() {
        return fxRate;
    }

    public String getForDate() {
        return forDate;
    }

    public String key() {
        return keyFrom(this.getFromCurrency(), this.getToCurrency());
    }

    public static String keyFrom(String fromCurrency, String toCurrency) {
        return fromCurrency + "_" + toCurrency;
    }

//    @Override
    public void toData(PdxWriter writer) {
        writer.writeString("fromCurrency",fromCurrency);
        writer.writeString("toCurrency",toCurrency);
        writer.writeObject("fxRate",fxRate);
        writer.writeString("forDate",forDate);
    }

    public void fromData(PdxReader reader) {

    }
}