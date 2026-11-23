
package com.example.tallyintegration.service;

import com.example.tallyintegration.dto.AddressDto;
import com.example.tallyintegration.dto.LedgerDto;
import com.example.tallyintegration.xmlconstants.TallyXmlConstants;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Service
public class TallyLedgerService {

    private final String url = "http://localhost:9000";

    @Value("${companyName}")
    private String companyName;

    @Value("${applicableFrom}")
    private String applicableFrom;

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

      AddressDto addressDto = dto.getAddressDto();

        if (addressDto != null) {
            logger.info("DEBUG ADDRESS -> Line1: {}, City: {}, State: {}, Country: {}, Pincode: {}",
                    addressDto.getAddressLine1(), addressDto.getCity(), addressDto.getState(), addressDto.getCountry(), addressDto.getPincode());
        } else {
            logger.error("DEBUG ADDRESS -> addressDto is NULL!");
        }
//        String uniqueId = "101"; // Default unique ID if not provided

        String xml = TallyXmlConstants.TALLY_LEDGER_CREATION
                .replace("{companyName}", escapeXml(companyName))
                .replace("{ledgerName}", escapeXml(dto.getName()))
                .replace("{uniqueId}", escapeXml(dto.getUniqueId()))

                .replace("{address1}", escapeXml(addressDto.getAddressLine1()))
                .replace("{address2}", escapeXml(addressDto.getAddressLine2()))
                .replace("{address3}", "")
                .replace("{address4}", escapeXml(addressDto.getCity()))

                .replace("{applicableFrom}", escapeXml(applicableFrom))
                .replace("{creditPeriod}", escapeXml(Objects.toString(creditPeriod, "")))
                .replace("{openingBalance}",
                        Objects.toString(dto.getOpeningBalance(), "0"))

                .replace("{country}", escapeXml(addressDto.getCountry()))
                .replace("{state}", escapeXml(addressDto.getState()))
                .replace("{pincode}", escapeXml(addressDto.getPincode()))

//                .replace("{phoneNumber}", escapeXml(dto.getMobileNo()))
//                .replace("{email}", escapeXml(dto.getEmailId()))

                .replace("{ledgerStateName}", escapeXml(addressDto.getState()))
                .replace("{gstRegistrationType}",
                        escapeXml(Objects.toString(dto.getGstRegistrationType(), "")))
                .replace("{gstin}", escapeXml(Objects.toString(dto.getGstin(), "")))
                .replace("{panNumber}", escapeXml(Objects.toString(dto.getPan(), "")))

                .replace("{ledgerParent}", escapeXml(defaultParent));

        logger.info("Creating Tally ledger for customer: {}",
                dto.getName());

//        System.out.println("xml: " +xml);

        String response = restClient.post()
                .uri(url)
                .header("Content-Type", "application/xml")
                .body(xml)
                .retrieve()
                .body(String.class);
        logger.info("Tally response: {}", response);
        return response;
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
