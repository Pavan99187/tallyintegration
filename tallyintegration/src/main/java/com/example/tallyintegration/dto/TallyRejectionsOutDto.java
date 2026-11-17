package com.example.tallyintegration.dto;

import java.util.List;

public class TallyRejectionsOutDto {

    private String companyName;
    private String supplierLedgerName;
    private String returnDate;
    private String returnNumber;
    private String originalGrnNumber;
    private String narration;
    private List<RejectionsOutItemDto> items;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSupplierLedgerName() {
        return supplierLedgerName;
    }

    public void setSupplierLedgerName(String supplierLedgerName) {
        this.supplierLedgerName = supplierLedgerName;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnNumber() {
        return returnNumber;
    }

    public void setReturnNumber(String returnNumber) {
        this.returnNumber = returnNumber;
    }

    public String getOriginalGrnNumber() {
        return originalGrnNumber;
    }

    public void setOriginalGrnNumber(String originalGrnNumber) {
        this.originalGrnNumber = originalGrnNumber;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public List<RejectionsOutItemDto> getItems() {
        return items;
    }

    public void setItems(List<RejectionsOutItemDto> items) {
        this.items = items;
    }
}
