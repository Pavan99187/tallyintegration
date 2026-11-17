
package com.example.tallyintegration.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class LedgerDto {

    @JsonProperty("company")
    private String company;

//    @JsonProperty("erpnext_customer_id")
//    private String erpnextCustomerId;

    @JsonProperty("name")
    @JsonAlias({"customer_name", "supplier_name"})
    private String name;

    @JsonProperty("ledger_group")
    private String ledgerGroup;

    @JsonProperty("address_line1")
    private String addressLine1;

    @JsonProperty("address_line2")
    private String addressLine2;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("country")
    private String country;

    @JsonProperty("pincode")
    private String pincode;

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    //    public String getErpnextCustomerId() {
    //        return erpnextCustomerId;
    //    }

    //    public void setErpnextCustomerId(String erpnextCustomerId) {
    //        this.erpnextCustomerId = erpnextCustomerId;
//    }

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

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
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
}
