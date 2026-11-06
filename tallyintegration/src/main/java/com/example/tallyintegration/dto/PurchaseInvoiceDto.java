
package com.example.tallyintegration.dto;

import java.util.List;

public class PurchaseInvoiceDto {

    private String companyName;
    private String supplierLedger;
    private String invoiceNumber;
    private String invoiceDate;
    private String purchaseLedger;
    private String sellerState;
    private String placeOfSupply;
    private List<SalesInvoiceItemDto> items;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSupplierLedger() {
        return supplierLedger;
    }

    public void setSupplierLedger(String supplierLedger) {
        this.supplierLedger = supplierLedger;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getPurchaseLedger() {
        return purchaseLedger;
    }

    public void setPurchaseLedger(String purchaseLedger) {
        this.purchaseLedger = purchaseLedger;
    }

    public String getSellerState() {
        return sellerState;
    }

    public void setSellerState(String sellerState) {
        this.sellerState = sellerState;
    }

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public List<SalesInvoiceItemDto> getItems() {
        return items;
    }

    public void setItems(List<SalesInvoiceItemDto> items) {
        this.items = items;
    }
}
