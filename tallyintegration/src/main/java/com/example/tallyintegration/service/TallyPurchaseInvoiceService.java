package com.example.tallyintegration.service;

import com.example.tallyintegration.dto.PurchaseInvoiceDto;
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
import java.util.Objects;

@Service
public class TallyPurchaseInvoiceService {

    private final RestClient restClient = RestClient.create();

    private static final String TALLY_URL = "http://localhost:9000";
    private static final String UNIT = "Nos";

    public String createPurchaseInvoice(PurchaseInvoiceDto dto, Logger logger) {

        validateInvoice(dto);

        String inventoryEntries = buildInventoryEntries(dto);
        String taxEntries = buildTaxEntries(dto);

        BigDecimal subtotal = dto.getItems().stream()
                .map(item -> amount(item.getQuantity(), item.getRate()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal taxTotal = dto.getItems().stream()
                .map(item -> calculateTax(
                        amount(item.getQuantity(), item.getRate()),
                        BigDecimal.valueOf(item.getGstRate())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAmount = subtotal.add(taxTotal)
                .setScale(2, RoundingMode.HALF_UP);

        String xml = TallyXmlConstants.TALLY_PURCHASE_INVOICE
                .replace("{companyName}", escapeXml(dto.getCompanyName()))
                .replace("{invoiceDate}", formatDate(dto.getInvoiceDate()))
                .replace("{invoiceNumber}", escapeXml(dto.getInvoiceNumber()))
                .replace("{supplierLedger}", escapeXml(dto.getSupplierLedger()))
                .replace("{companyState}", escapeXml(dto.getSellerState()))
                .replace("{placeOfSupply}", escapeXml(dto.getPlaceOfSupply()))
                .replace("{inventoryEntries}", inventoryEntries)
                .replace("{taxEntries}", taxEntries)
                .replace("{totalAmount}", totalAmount.toPlainString());

        logger.info("Subtotal: {}", subtotal);
        logger.info("Tax total: {}", taxTotal);
        logger.info("Invoice total: {}", totalAmount);
        logger.info("Purchase XML:\n{}", xml);

        return restClient.post()
                .uri(TALLY_URL)
                .contentType(MediaType.APPLICATION_XML)
                .body(xml)
                .retrieve()
                .body(String.class);
    }

    private String buildInventoryEntries(PurchaseInvoiceDto dto) {

        StringBuilder entries = new StringBuilder();

        for (SalesInvoiceItemDto item : dto.getItems()) {

            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            BigDecimal rate = BigDecimal.valueOf(item.getRate());

            BigDecimal itemAmount = amount(
                    item.getQuantity(), item.getRate());

            entries.append("<ALLINVENTORYENTRIES.LIST>")
                    .append("<STOCKITEMNAME>")
                    .append(escapeXml(item.getItemName()))
                    .append("</STOCKITEMNAME>")
                    .append("<ISDEEMEDPOSITIVE>Yes</ISDEEMEDPOSITIVE>")
                    .append("<RATE>")
                    .append(rate.toPlainString())
                    .append("/")
                    .append(UNIT)
                    .append("</RATE>")
                    .append("<AMOUNT>-")
                    .append(itemAmount.toPlainString())
                    .append("</AMOUNT>")
                    .append("<ACTUALQTY>")
                    .append(quantity.stripTrailingZeros().toPlainString())
                    .append(" ")
                    .append(UNIT)
                    .append("</ACTUALQTY>")
                    .append("<BILLEDQTY>")
                    .append(quantity.stripTrailingZeros().toPlainString())
                    .append(" ")
                    .append(UNIT)
                    .append("</BILLEDQTY>")
                    .append("<ACCOUNTINGALLOCATIONS.LIST>")
                    .append("<LEDGERNAME>")
                    .append(escapeXml(dto.getPurchaseLedger()))
                    .append("</LEDGERNAME>")
                    .append("<ISDEEMEDPOSITIVE>Yes</ISDEEMEDPOSITIVE>")
                    .append("<AMOUNT>-")
                    .append(itemAmount.toPlainString())
                    .append("</AMOUNT>")
                    .append("</ACCOUNTINGALLOCATIONS.LIST>")
                    .append("</ALLINVENTORYENTRIES.LIST>");
        }

        return entries.toString();
    }

    private String buildTaxEntries(PurchaseInvoiceDto dto) {

        StringBuilder entries = new StringBuilder();

        // Compare the company's GST registration state with place of supply.
        boolean intraState = dto.getSellerState().trim()
                .equalsIgnoreCase(dto.getPlaceOfSupply().trim());

        for (SalesInvoiceItemDto item : dto.getItems()) {

            BigDecimal taxableAmount = amount(
                    item.getQuantity(), item.getRate());

            BigDecimal gstRate = BigDecimal.valueOf(item.getGstRate());

            if (intraState) {

                BigDecimal halfRate = gstRate.divide(
                        BigDecimal.valueOf(2),
                        4,
                        RoundingMode.HALF_UP);

                BigDecimal cgst = calculateTax(taxableAmount, halfRate);
                BigDecimal sgst = calculateTax(taxableAmount, halfRate);

                appendTaxEntry(
                        entries,
                        "CGST " + formatRate(halfRate) + "%",
                        cgst,
                        halfRate);

                appendTaxEntry(
                        entries,
                        "SGST " + formatRate(halfRate) + "%",
                        sgst,
                        halfRate);

            } else {

                BigDecimal igst = calculateTax(taxableAmount, gstRate);

                appendTaxEntry(
                        entries,
                        "IGST " + formatRate(gstRate) + "%",
                        igst,
                        gstRate);
            }
        }

        return entries.toString();
    }

    private void appendTaxEntry(
            StringBuilder entries,
            String ledgerName,
            BigDecimal tax,
            BigDecimal taxRate) {

        if (tax.signum() == 0) {
            return;
        }

        String taxAmount = tax.setScale(2, RoundingMode.HALF_UP)
                .toPlainString();

        entries.append("<LEDGERENTRIES.LIST>")

                .append("<RATEOFINVOICETAX.LIST TYPE=\"Number\">")
                .append("<RATEOFINVOICETAX>")
                .append(formatRate(taxRate))
                .append("</RATEOFINVOICETAX>")
                .append("</RATEOFINVOICETAX.LIST>")

                .append("<LEDGERNAME>")
                .append(escapeXml(ledgerName))
                .append("</LEDGERNAME>")

                .append("<ISDEEMEDPOSITIVE>Yes</ISDEEMEDPOSITIVE>")

                .append("<AMOUNT>-")
                .append(taxAmount)
                .append("</AMOUNT>")

                .append("<VATEXPAMOUNT>-")
                .append(taxAmount)
                .append("</VATEXPAMOUNT>")

                .append("</LEDGERENTRIES.LIST>");
    }

    private BigDecimal amount(double quantity, double rate) {
        return BigDecimal.valueOf(quantity)
                .multiply(BigDecimal.valueOf(rate))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateTax(
            BigDecimal taxableAmount,
            BigDecimal rate) {

        return taxableAmount.multiply(rate)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    private String formatRate(BigDecimal rate) {
        return rate.stripTrailingZeros().toPlainString();
    }

    private String formatDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
                .format(DateTimeFormatter.BASIC_ISO_DATE);
    }

    private void validateInvoice(PurchaseInvoiceDto dto) {

        Objects.requireNonNull(dto, "Purchase invoice cannot be null");

        requireText(dto.getCompanyName(), "Company name");
        requireText(dto.getSupplierLedger(), "Supplier ledger");
        requireText(dto.getInvoiceNumber(), "Invoice number");
        requireText(dto.getInvoiceDate(), "Invoice date");
        requireText(dto.getPurchaseLedger(), "Purchase ledger");
        requireText(dto.getSellerState(), "Company GST registration state");
        requireText(dto.getPlaceOfSupply(), "Place of supply");

        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new IllegalArgumentException(
                    "Purchase invoice must contain at least one item");
        }

        for (SalesInvoiceItemDto item : dto.getItems()) {

            if (item == null) {
                throw new IllegalArgumentException(
                        "Purchase invoice item cannot be null");
            }

            requireText(item.getItemName(), "Item name");

            if (!Double.isFinite(item.getQuantity())
                    || item.getQuantity() <= 0) {
                throw new IllegalArgumentException(
                        "Quantity must be greater than zero");
            }

            if (!Double.isFinite(item.getRate()) || item.getRate() < 0) {
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