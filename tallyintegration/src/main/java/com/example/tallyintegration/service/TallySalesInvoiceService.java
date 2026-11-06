package com.example.tallyintegration.service;

import com.example.tallyintegration.dto.SalesInvoiceDto;
import com.example.tallyintegration.dto.SalesInvoiceItemDto;
import com.example.tallyintegration.xmlconstants.TallyXmlConstants;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

    @Service
    public class TallySalesInvoiceService {

        private final RestClient restClient;

        public TallySalesInvoiceService() {
            this.restClient = RestClient.create();
        }

        private static final String TALLY_URL = "http://localhost:9000";
        private static final String UNIT = "Nos";

        public String createSalesInvoice(SalesInvoiceDto dto, Logger logger) {
            validateInvoice(dto);

            String inventoryEntries = buildInventoryEntries(dto);
            String taxEntries = buildTaxEntries(dto);

            BigDecimal subtotal = dto.getItems().stream()
                    .map(item -> BigDecimal.valueOf(item.getQuantity())
                            .multiply(BigDecimal.valueOf(item.getRate()))
                            .setScale(2, RoundingMode.HALF_UP))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal taxTotal = dto.getItems().stream()
                    .map(item -> {
                        BigDecimal taxableAmount =
                                BigDecimal.valueOf(item.getQuantity())
                                        .multiply(BigDecimal.valueOf(item.getRate()))
                                        .setScale(2, RoundingMode.HALF_UP);

                        return calculateTax(
                                taxableAmount,
                                BigDecimal.valueOf(item.getGstRate()));
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalAmount = subtotal.add(taxTotal)
                    .setScale(2, RoundingMode.HALF_UP);

            logger.info("Generated GST entries:\n{}", taxEntries);

            String xml = TallyXmlConstants.TALLY_SALES_INVOICE
                    .replace("{companyName}", escapeXml(dto.getCompanyName()))
                    .replace("{invoiceDate}", formatDate(dto.getInvoiceDate()))
                    .replace("{invoiceNumber}", escapeXml(dto.getInvoiceNumber()))
                    .replace("{customerLedger}", escapeXml(dto.getCustomerLedger()))
                    .replace("{inventoryEntries}", inventoryEntries)
                    .replace("{taxEntries}", taxEntries)
                    .replace("{totalAmount}", totalAmount.toPlainString());

            logger.info("Sending Sales Invoice XML to Tally:\n{}", xml);

            String response =  restClient.post()
                    .uri(TALLY_URL)
                    .contentType(MediaType.APPLICATION_XML)
                    .body(xml)
                    .retrieve()
                    .body(String.class);
            return response;
        }

        private String buildInventoryEntries(SalesInvoiceDto dto) {

            StringBuilder entries = new StringBuilder();

            for (SalesInvoiceItemDto item : dto.getItems()) {

                BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
                BigDecimal rate = BigDecimal.valueOf(item.getRate());

                BigDecimal amount = quantity.multiply(rate)
                        .setScale(2, RoundingMode.HALF_UP);

                entries.append(
                                "<ALLINVENTORYENTRIES.LIST>")
                        .append("<STOCKITEMNAME>")
                        .append(escapeXml(item.getItemName()))
                        .append("</STOCKITEMNAME>")
                        .append("<ISDEEMEDPOSITIVE>No</ISDEEMEDPOSITIVE>")
                        .append("<RATE>")
                        .append(rate.toPlainString())
                        .append("/")
                        .append(UNIT)
                        .append("</RATE>")
                        .append("<AMOUNT>")
                        .append(amount.toPlainString())
                        .append("</AMOUNT>")
                        .append("<ACTUALQTY>")
                        .append(quantity.stripTrailingZeros().toPlainString())
                        .append(" ").append(UNIT)
                        .append("</ACTUALQTY>")
                        .append("<BILLEDQTY>")
                        .append(quantity.stripTrailingZeros().toPlainString())
                        .append(" ").append(UNIT)
                        .append("</BILLEDQTY>")
                        .append("<ACCOUNTINGALLOCATIONS.LIST>")
                        .append("<LEDGERNAME>")
                        .append(escapeXml(dto.getSalesLedger()))
                        .append("</LEDGERNAME>")
                        .append("<ISDEEMEDPOSITIVE>No</ISDEEMEDPOSITIVE>")
                        .append("<AMOUNT>")
                        .append(amount.toPlainString())
                        .append("</AMOUNT>")
                        .append("</ACCOUNTINGALLOCATIONS.LIST>")
                        .append("</ALLINVENTORYENTRIES.LIST>");
            }

            return entries.toString();
        }

        private String buildTaxEntries(SalesInvoiceDto dto) {

            StringBuilder entries = new StringBuilder();

            boolean intraState = dto.getSellerState().trim()
                    .equalsIgnoreCase(dto.getPlaceOfSupply().trim());

            for (SalesInvoiceItemDto item : dto.getItems()) {

                BigDecimal taxableAmount =
                        BigDecimal.valueOf(item.getQuantity())
                                .multiply(BigDecimal.valueOf(item.getRate()))
                                .setScale(2, RoundingMode.HALF_UP);

                BigDecimal gstRate = BigDecimal.valueOf(item.getGstRate());

                if (gstRate.signum() < 0) {
                    throw new IllegalArgumentException(
                            "GST rate cannot be negative");
                }

                if (intraState) {
                    BigDecimal halfRate = gstRate.divide(
                            BigDecimal.valueOf(2), 4, RoundingMode.HALF_UP);

                    BigDecimal cgst = calculateTax(taxableAmount, halfRate);
                    BigDecimal sgst = calculateTax(taxableAmount, halfRate);

                    appendTaxEntry(entries, "CGST "
                            + formatRate(halfRate) + "%", cgst);
                    appendTaxEntry(entries, "SGST "
                            + formatRate(halfRate) + "%", sgst);

                } else {
                    BigDecimal igst = calculateTax(taxableAmount, gstRate);

                    appendTaxEntry(entries, "IGST "
                            + formatRate(gstRate) + "%", igst);
                }
            }

            return entries.toString();
        }

        private BigDecimal calculateTax(
                BigDecimal taxableAmount, BigDecimal rate) {

            return taxableAmount.multiply(rate)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }

        private void appendTaxEntry(
                StringBuilder entries, String ledgerName, BigDecimal amount) {

            if (amount.signum() == 0) {
                return;
            }

            entries.append("<LEDGERENTRIES.LIST>")
                    .append("<LEDGERNAME>")
                    .append(escapeXml(ledgerName))
                    .append("</LEDGERNAME>")
                    .append("<ISDEEMEDPOSITIVE>No</ISDEEMEDPOSITIVE>")
                    .append("<AMOUNT>")
                    .append(amount.toPlainString())
                    .append("</AMOUNT>")
                    .append("</LEDGERENTRIES.LIST>");
        }

        private String formatRate(BigDecimal rate) {
            return rate.stripTrailingZeros().toPlainString();
        }

        private String formatDate(String date) {
            // Accept yyyy-MM-dd and convert it to Tally's yyyyMMdd format.
            return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
                    .format(DateTimeFormatter.BASIC_ISO_DATE);
        }

        private void validateInvoice(SalesInvoiceDto dto) {

            Objects.requireNonNull(dto, "Invoice cannot be null");

            requireText(dto.getCompanyName(), "Company name");
            requireText(dto.getCustomerLedger(), "Customer ledger");
            requireText(dto.getInvoiceNumber(), "Invoice number");
            requireText(dto.getInvoiceDate(), "Invoice date");
            requireText(dto.getSalesLedger(), "Sales ledger");
            requireText(dto.getSellerState(), "Seller state");
            requireText(dto.getPlaceOfSupply(), "Place of supply");

            if (dto.getItems() == null || dto.getItems().isEmpty()) {
                throw new IllegalArgumentException(
                        "Invoice must contain at least one item");
            }

            for (SalesInvoiceItemDto item : dto.getItems()) {

                if (item == null) {
                    throw new IllegalArgumentException(
                            "Invoice item cannot be null");
                }

                requireText(item.getItemName(), "Item name");

                if (!Double.isFinite(item.getQuantity())
                        || item.getQuantity() <= 0) {
                    throw new IllegalArgumentException(
                            "Quantity must be greater than zero");
                }

                if (!Double.isFinite(item.getRate())
                        || item.getRate() < 0) {
                    throw new IllegalArgumentException(
                            "Rate cannot be negative");
                }

                if (!Double.isFinite(item.getGstRate())
                        || item.getGstRate() < 0
                        || item.getGstRate() > 100) {
                    throw new IllegalArgumentException(
                            "GST rate must be between 0 and 100");
                }
            }

            // Validate date before sending a request to Tally.
            formatDate(dto.getInvoiceDate());
        }

        private void requireText(String value, String fieldName) {
            if (value == null || value.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        fieldName + " is required");
            }
        }

        private String escapeXml(String value) {
            return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
        }
    }

