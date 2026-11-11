package com.example.tallyintegration.dto;

import java.math.BigDecimal;

public class TaxDto {


    private String ledgerName;
    private BigDecimal rate;     // NEW: 18 for IGST, 9 for CGST/SGST
    private BigDecimal amount;

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
