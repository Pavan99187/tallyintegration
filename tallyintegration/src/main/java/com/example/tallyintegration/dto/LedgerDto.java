
package com.example.tallyintegration.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class LedgerDto {

    @JsonProperty("company")
    private String company;

    @JsonProperty("unique_id")
    private String uniqueId;

    @JsonProperty("name")
    @JsonAlias({"customer_name", "supplier_name"})
    private String name;

    @JsonProperty("ledger_group")
    private String ledgerGroup;

    @JsonProperty("mobile_no")
    private String mobileNo;

    @JsonProperty("email_id")
    private String emailId;

    @JsonProperty("tax_id")
    @JsonAlias({"gstin"})
    private String gstin;

    @JsonProperty("pan")
    private String pan;

    @JsonProperty("gst_registration_type")
    @JsonAlias({"gst_category"})
    private String gstRegistrationType;

    @JsonProperty("applicable_from")
    private String applicableFrom;

    @JsonProperty("opening_balance")
    private BigDecimal openingBalance;

    @JsonProperty("credit_days")
    private Integer creditDays;

    @JsonProperty("address")
    private AddressDto addressDto;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLedgerGroup() {
        return ledgerGroup;
    }

    public void setLedgerGroup(String ledgerGroup) {
        this.ledgerGroup = ledgerGroup;
    }


    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getGstRegistrationType() {
        return gstRegistrationType;
    }

    public void setGstRegistrationType(String gstRegistrationType) {
        this.gstRegistrationType = gstRegistrationType;
    }

    public String getApplicableFrom() {
        return applicableFrom;
    }

    public void setApplicableFrom(String applicableFrom) {
        this.applicableFrom = applicableFrom;
    }

    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    public Integer getCreditDays() {
        return creditDays;
    }

    public void setCreditDays(Integer creditDays) {
        this.creditDays = creditDays;
    }

    public AddressDto getAddressDto() {
        return addressDto;
    }

    public void setAddressDto(AddressDto addressDto) {
        this.addressDto = addressDto;
    }
}
