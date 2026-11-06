package com.example.tallyintegration.service;

import com.example.tallyintegration.dto.TallyGrnDto;
import com.example.tallyintegration.dto.SalesInvoiceItemDto;
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
public class TallyGrnService {

    private final RestClient restClient;

    private final String url = "http://localhost:9000";

    public TallyGrnService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String createGrn(TallyGrnDto dto, Logger logger) {

        // Build inventory entries
        String inventoryEntries = buildGrnInventoryEntries(dto);

        // Build complete XML
        String xml = TallyXmlConstants.TALLY_GRN
                .replace("{companyName}", escapeXml(dto.getCompanyName()))
                .replace("{grnDate}", formatDate(dto.getGrnDate()))
                .replace("{grnNumber}", escapeXml(dto.getGrnNumber()))
                .replace("{purchaseOrderNumber}", escapeXml(dto.getPurchaseOrderNumber()))
                .replace("{supplierLedgerName}", escapeXml(dto.getSupplierLedgerName()))
                .replace("{inventoryEntries}", inventoryEntries)
                .replace("{narration}", escapeXml(dto.getNarration()));

        logger.info("Generated GRN XML: {}", xml);

        // Send XML to TallyPrime
        String response = restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_XML)
                .body(xml)
                .retrieve()
                .body(String.class);

        return response;
    }

    private String buildGrnInventoryEntries(TallyGrnDto dto) {

        StringBuilder xml = new StringBuilder();

        List<SalesInvoiceItemDto> items =
                dto.getSalesInvoiceItemDtoList();

        for (SalesInvoiceItemDto item : items) {

            BigDecimal amount = BigDecimal.valueOf(item.getRate())
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            xml.append(
                    "<ALLINVENTORYENTRIES.LIST>" +
                            "<STOCKITEMNAME>" + escapeXml(item.getItemName()) + "</STOCKITEMNAME>" +
                            "<ISDEEMEDPOSITIVE>Yes</ISDEEMEDPOSITIVE>" +
                            "<RATE>" + item.getRate() + "/Nos</RATE>" +
                            "<ACTUALQTY>" + item.getQuantity() + " Nos</ACTUALQTY>" +
                            "<BILLEDQTY>" + item.getQuantity() + " Nos</BILLEDQTY>" +
                            "<AMOUNT>" + amount.toPlainString() + "</AMOUNT>" +
                            "<ACCOUNTINGALLOCATIONS.LIST> " +
                            "<LEDGERNAME>" + escapeXml(dto.getPurchaseLedger()) + "</LEDGERNAME>" +
                            "<AMOUNT>" + amount.toPlainString() + "</AMOUNT>" +
                            "</ACCOUNTINGALLOCATIONS.LIST>" +
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
        return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
                .format(DateTimeFormatter.BASIC_ISO_DATE);
    }

}