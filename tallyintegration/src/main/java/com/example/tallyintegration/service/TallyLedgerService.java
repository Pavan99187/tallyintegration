
package com.example.tallyintegration.service;

import com.example.tallyintegration.dto.LedgerDto;
import com.example.tallyintegration.xmlconstants.TallyXmlConstants;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Service
public class TallyLedgerService {

    private final String url = "http://localhost:9000";

    private final RestClient restClient;

    public TallyLedgerService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String createLedger(
            LedgerDto dto,
            String ledgerType,
            Logger logger) {

        String defaultParent;

        if ("CUSTOMER".equalsIgnoreCase(ledgerType)) {
            defaultParent = "Sundry Debtors";
        } else {
            defaultParent = "Sundry Creditors";
        }

        // Use the supplied Tally ledger group, or the default.
        String ledgerParent = hasText(dto.getLedgerGroup())
                ? dto.getLedgerGroup()
                : defaultParent;

        // Convert credit days into Tally credit-period format.
        String creditPeriod = dto.getCreditDays() == null
                ? ""
                : dto.getCreditDays() + " Days";

        String xml = TallyXmlConstants.TALLY_LEDGER_CREATION
                .replace("{companyName}", escapeXml(dto.getCompany()))
                .replace("{ledgerName}", escapeXml(dto.getName()))

                .replace("{address1}", escapeXml(dto.getAddressLine1()))
                .replace("{address2}", escapeXml(dto.getAddressLine2()))
                .replace("{address3}", "")
                .replace("{address4}", escapeXml(dto.getCity()))

                .replace("{applicableFrom}", escapeXml(dto.getApplicableFrom()))
                .replace("{creditPeriod}", escapeXml(creditPeriod))
                .replace("{openingBalance}",
                        Objects.toString(dto.getOpeningBalance(), "0"))

                .replace("{country}", escapeXml(dto.getCountry()))
                .replace("{state}", escapeXml(dto.getState()))
                .replace("{pincode}", escapeXml(dto.getPincode()))

                .replace("{phoneNumber}", escapeXml(dto.getMobileNo()))
                .replace("{email}", escapeXml(dto.getEmailId()))

                .replace("{ledgerStateName}", escapeXml(dto.getState()))
                .replace("{gstRegistrationType}",
                        escapeXml(dto.getGstRegistrationType()))
                .replace("{gstin}", escapeXml(dto.getGstin()))
                .replace("{panNumber}", escapeXml(dto.getPan()))

                .replace("{ledgerParent}", escapeXml(ledgerParent));

        logger.info("Creating Tally ledger for customer: {}",
                dto.getName());

        logger.debug("Tally Ledger XML: {}", xml);

        return restClient.post()
                .uri(url)
                .header("Content-Type", "application/xml")
                .body(xml)
                .retrieve()
                .body(String.class);
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private String escapeXml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
