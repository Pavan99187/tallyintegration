package com.example.tallyintegration.dto;

public class LedgerDto {

  private String companyName;
  private String ledgerName;
  private String address1;
  private String address2;
  private String address3;
  private String address4;
  private String creditPeriod;
  private String openingBalance;
  private String country;
  private String state;
  private String pincode;
  private String phoneNumber;
  private String email;
  private String ledgerStateName;
  private String gstRegistrationType;
  private String gstin;
  private String panNumber;
  private String applicableFrom;

    public LedgerDto() {
    }

    public LedgerDto(String companyName, String ledgerName, String address1, String address2, String address3, String address4, String creditPeriod, String openingBalance, String country, String state, String pincode, String phoneNumber, String email, String ledgerStateName, String gstRegistrationType, String gstin, String panNumber, String applicableFrom) {
        this.companyName = companyName;
        this.ledgerName = ledgerName;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.address4 = address4;
        this.creditPeriod = creditPeriod;
        this.openingBalance = openingBalance;
        this.country = country;
        this.state = state;
        this.pincode = pincode;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.ledgerStateName = ledgerStateName;
        this.gstRegistrationType = gstRegistrationType;
        this.gstin = gstin;
        this.panNumber = panNumber;
        this.applicableFrom = applicableFrom;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getGstRegistrationType() {
        return gstRegistrationType;
    }

    public void setGstRegistrationType(String gstRegistrationType) {
        this.gstRegistrationType = gstRegistrationType;
    }

    public String getLedgerStateName() {
        return ledgerStateName;
    }

    public void setLedgerStateName(String ledgerStateName) {
        this.ledgerStateName = ledgerStateName;
    }

    public String getApplicableFrom() {
        return applicableFrom;
    }

    public void setApplicableFrom(String applicableFrom) {
        this.applicableFrom = applicableFrom;
    }

    public String getCreditPeriod() {
        return creditPeriod;
    }

    public void setCreditPeriod(String creditPeriod) {
        this.creditPeriod = creditPeriod;
    }

    public String getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(String openingBalance) {
        this.openingBalance = openingBalance;
    }


}
