package com.example.tallyintegration.service;

import com.example.tallyintegration.dto.RejectionsOutItemDto;
import com.example.tallyintegration.dto.TallyRejectionsOutDto;
import com.example.tallyintegration.xmlconstants.TallyXmlConstants;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TallyRejectionsOutService {

    private static final String url = "http://localhost:9000";

    private final RestClient restClient;

    public TallyRejectionsOutService(RestClient restClient) {
        this.restClient = restClient;
    }


    public String createRejectionsOut(
            TallyRejectionsOutDto dto, Logger logger) {

        String inventoryEntries = buildInventoryEntries(dto);

        String narration = dto.getNarration();

        if (dto.getOriginalGrnNumber() != null &&
                !dto.getOriginalGrnNumber().isBlank()) {

            narration = "Return against GRN: "
                    + dto.getOriginalGrnNumber()
                    + " - " + narration;
        }

        String xml = TallyXmlConstants.TALLY_REJECTIONS_OUT
                .replace("{companyName}",
                        escapeXml(dto.getCompanyName()))
                .replace("{returnDate}",
                        formatDate(dto.getReturnDate()))
                .replace("{returnNumber}",
                        escapeXml(dto.getReturnNumber()))
                .replace("{supplierLedgerName}",
                        escapeXml(dto.getSupplierLedgerName()))
                .replace("{inventoryEntries}", inventoryEntries)
                .replace("{narration}", escapeXml(narration));

        logger.info("Generated Rejections Out XML: {}", xml);

        String response = restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_XML)
                .body(xml)
                .retrieve()
                .body(String.class);

        return response;
    }

    private String buildInventoryEntries(
            TallyRejectionsOutDto dto) {

        StringBuilder xml = new StringBuilder();

        List<RejectionsOutItemDto> items = dto.getItems();

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one return item is required");
        }

        for (RejectionsOutItemDto item : items) {

            if (item.getQuantity() == null ||
                    item.getRate() == null ||
                    item.getQuantity().compareTo(BigDecimal.ZERO) <= 0 ||
                    item.getRate().compareTo(BigDecimal.ZERO) < 0) {

                throw new IllegalArgumentException(
                        "Invalid quantity or rate for "
                                + item.getItemName());
            }

            BigDecimal amount = item.getRate()
                    .multiply(item.getQuantity());

            xml.append(
                    "<ALLINVENTORYENTRIES.LIST>" +

                            "<STOCKITEMNAME>" +
                            escapeXml(item.getItemName()) +
                            "</STOCKITEMNAME>" +

                            "<ISDEEMEDPOSITIVE>No</ISDEEMEDPOSITIVE>" +

                            "<RATE>" +
                            item.getRate().toPlainString() +
                            "/Nos</RATE>" +

                            "<AMOUNT>" +
                            amount.toPlainString() +
                            "</AMOUNT>" +

                            "<ACTUALQTY>" +
                            item.getQuantity().toPlainString() +
                            " Nos</ACTUALQTY>" +

                            "<BILLEDQTY>" +
                            item.getQuantity().toPlainString() +
                            " Nos</BILLEDQTY>" +

                            "<BATCHALLOCATIONS.LIST>" +

                            "<GODOWNNAME>" +
                            escapeXml(item.getGodownName()) +
                            "</GODOWNNAME>" +

                            "<BATCHNAME>" +
                            escapeXml(item.getBatchName()) +
                            "</BATCHNAME>" +

                            "<AMOUNT>" +
                            amount.toPlainString() +
                            "</AMOUNT>" +

                            "<ACTUALQTY>" +
                            item.getQuantity().toPlainString() +
                            " Nos</ACTUALQTY>" +

                            "<BILLEDQTY>" +
                            item.getQuantity().toPlainString() +
                            " Nos</BILLEDQTY>" +

                            "</BATCHALLOCATIONS.LIST>" +

                            "</ALLINVENTORYENTRIES.LIST>"
            );
        }

        return xml.toString();
    }

    private String escapeXml(String value) {

        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    private String formatDate(String date) {

        return LocalDate.parse(
                        date, DateTimeFormatter.ISO_LOCAL_DATE)
                .format(DateTimeFormatter.BASIC_ISO_DATE);
    }




}
