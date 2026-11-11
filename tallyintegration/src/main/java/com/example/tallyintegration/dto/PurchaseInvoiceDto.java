package com.example.tallyintegration.dto;

import java.math.BigDecimal;
import java.util.List;

public class PurchaseInvoiceDto {
    private String companyName;
    private String supplierLedger;
    private String supplierGstin;            // optional
    private String supplierState;            // new, optional (supplier's state, e.g. "Karnataka")
    private String invoiceNumber;
    private String invoiceDate;              // yyyy-MM-dd
    private String supplierInvoiceNumber;    // new, optional
    private String supplierInvoiceDate;      // new, optional (yyyy-MM-dd)
    private String purchaseLedger;
    private String placeOfSupply;
    private String companyState;             // optional (renamed from sellerState)
    private List<SalesInvoiceItemDto> items;
    private List<TaxDto> taxes;              // new
    private BigDecimal roundOff;             // new, optional
    private BigDecimal grandTotal;           // new

    // getters and setters for every field


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

    public String getSupplierGstin() {
        return supplierGstin;
    }

    public void setSupplierGstin(String supplierGstin) {
        this.supplierGstin = supplierGstin;
    }

    public String getSupplierState() {
        return supplierState;
    }

    public void setSupplierState(String supplierState) {
        this.supplierState = supplierState;
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

    public String getSupplierInvoiceNumber() {
        return supplierInvoiceNumber;
    }

    public void setSupplierInvoiceNumber(String supplierInvoiceNumber) {
        this.supplierInvoiceNumber = supplierInvoiceNumber;
    }

    public String getSupplierInvoiceDate() {
        return supplierInvoiceDate;
    }

    public void setSupplierInvoiceDate(String supplierInvoiceDate) {
        this.supplierInvoiceDate = supplierInvoiceDate;
    }

    public String getPurchaseLedger() {
        return purchaseLedger;
    }

    public void setPurchaseLedger(String purchaseLedger) {
        this.purchaseLedger = purchaseLedger;
    }

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public String getCompanyState() {
        return companyState;
    }

    public void setCompanyState(String companyState) {
        this.companyState = companyState;
    }

    public List<SalesInvoiceItemDto> getItems() {
        return items;
    }

    public void setItems(List<SalesInvoiceItemDto> items) {
        this.items = items;
    }

    public List<TaxDto> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<TaxDto> taxes) {
        this.taxes = taxes;
    }

    public BigDecimal getRoundOff() {
        return roundOff;
    }

    public void setRoundOff(BigDecimal roundOff) {
        this.roundOff = roundOff;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }
}