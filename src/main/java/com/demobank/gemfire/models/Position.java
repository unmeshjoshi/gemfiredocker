package com.demobank.gemfire.models;

import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxWriter;

import java.math.BigDecimal;

public class Position {
    private Integer accountKey;
    private String accountNumber;
    private PositionType accountType;

    private String assetClassL1;
    private String assetClassL2;
    private String securityId;
    private Integer quantity;
    private String accountGroupId;
    private BigDecimal balance;
    private String currency;
    private String positionDate;
    private FxRate rate = new FxRate("USD", "AUS", new BigDecimal(2), "2018-01-28");

    public Position() {}

    public Position(Integer accountKey, PositionType accountType, String accountNumber, String assetClassL1, String assetClassL2, String securityId, Integer quantity, String accountGroupId, BigDecimal balance, String currency, String positionDate)  {
        this.accountKey = accountKey;
        this.accountNumber = accountNumber;
        this.assetClassL1 = assetClassL1;
        this.assetClassL2 = assetClassL2;
        this.securityId = securityId;
        this.quantity = quantity;
        this.accountGroupId = accountGroupId;
        this.balance = balance;
        this.currency = currency;
        this.positionDate = positionDate;
        this.accountType = accountType;
    }

    public String key() {
        return accountKey + "";
    }

    public Integer getAccountKey() {
        return accountKey;
    }

    public PositionType getAccountType() {
        return accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAssetClassL1() {
        return assetClassL1;
    }

    public String getAssetClassL2() {
        return assetClassL2;
    }

    public String getSecurityId() {
        return securityId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getAccountGroupId() {
        return accountGroupId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPositionDate() {
        return positionDate;
    }

    public int calculateBalance() {
        return balance.multiply(BigDecimal.valueOf(2)).intValue();
    }

    public void toData(PdxWriter writer) {
        writer.writeInt("accountKey", this.accountKey);
        writer.writeObject("accountType", accountType.toString());
        writer.writeString("accountNumber", accountNumber);
        writer.writeString("assetClassL1", assetClassL1);
        writer.writeString("assetClassL2", assetClassL2);
        writer.writeString("securityId", securityId);
        writer.writeInt("quantity", quantity);
        writer.writeString("accountGroupId", accountGroupId);
        writer.writeObject("balance", balance);
        writer.writeString("currency", currency);
        writer.writeString("positionDate", positionDate);
        writer.writeObject("fxRate", rate);
     }

    public void fromData(PdxReader reader) {

    }
}
