package com.example.tallyintegration.dto;

import java.util.List;

public class TallyGrnDto {

    private String companyName;
    private String supplierLedgerName;
    private String grnDate;
    private String grnNumber;
    private String purchaseOrderNumber;
    private String purchaseLedger;
    private String narration;
    private List<GrnItemDto> grnItemDtoList;

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

    public String getGrnDate() {
        return grnDate;
    }

    public void setGrnDate(String grnDate) {
        this.grnDate = grnDate;
    }

    public String getGrnNumber() {
        return grnNumber;
    }

    public void setGrnNumber(String grnNumber) {
        this.grnNumber = grnNumber;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public String getPurchaseLedger() {
        return purchaseLedger;
    }

    public void setPurchaseLedger(String purchaseLedger) {
        this.purchaseLedger = purchaseLedger;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public List<GrnItemDto> getGrnItemDtoList() {
        return grnItemDtoList;
    }

    public void setGrnItemDtoList(List<GrnItemDto> grnItemDtoList) {
        this.grnItemDtoList = grnItemDtoList;
    }
}
