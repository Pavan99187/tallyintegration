package com.example.tallyintegration.service;

import com.example.tallyintegration.dto.LedgerDto;
import com.example.tallyintegration.xmlconstants.TallyXmlConstants;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Service
public class TallyLedgerService {

    String url = "http://localhost:9000";

    private final RestClient restClient;

    public TallyLedgerService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String createLedger(
            LedgerDto dto,
            String ledgerType,
            Logger logger) {

        String ledgerParent;

        if ("CUSTOMER".equalsIgnoreCase(ledgerType)) {
            ledgerParent = "Sundry Debtors";
        } else {
            ledgerParent = "Sundry Creditors";
        }

        String xml = TallyXmlConstants.TALLY_LEDGER_CREATION
                .replace("{companyName}", dto.getCompanyName())
                .replace("{ledgerName}", dto.getLedgerName())
                .replace("{address1}", Objects.toString(dto.getAddress1(), ""))
                .replace("{address2}", Objects.toString(dto.getAddress2(), ""))
                .replace("{address3}", Objects.toString(dto.getAddress3(), ""))
                .replace("{address4}", Objects.toString(dto.getAddress4(), ""))
                .replace("{applicableFrom}", Objects.toString(dto.getApplicableFrom(),""))
                .replace("{creditPeriod}", Objects.toString(dto.getCreditPeriod(),""))
                .replace("{openingBalance}", Objects.toString(dto.getOpeningBalance(),""))
                .replace("{country}", dto.getCountry())
                .replace("{state}", dto.getState())
                .replace("{pincode}", Objects.toString(dto.getPincode(), ""))
                .replace("{phoneNumber}", Objects.toString(dto.getPhoneNumber(), ""))
                .replace("{email}", Objects.toString(dto.getEmail(), ""))
                .replace("{ledgerStateName}", dto.getLedgerStateName())
                .replace("{gstRegistrationType}", Objects.toString(dto.getGstRegistrationType(), ""))
                .replace("{gstin}", Objects.toString(dto.getGstin(), ""))
                .replace("{panNumber}", Objects.toString(dto.getPanNumber(), ""))
                .replace("{ledgerParent}", ledgerParent);

        return restClient.post()
                .uri(url)
                .header("Content-Type", "application/xml")
                .body(xml)
                .retrieve()
                .body(String.class);
    }




}
