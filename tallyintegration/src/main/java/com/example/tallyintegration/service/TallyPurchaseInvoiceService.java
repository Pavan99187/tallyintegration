package com.example.tallyintegration.service;

import com.example.tallyintegration.dto.PurchaseInvoiceDto;
import com.example.tallyintegration.dto.SalesInvoiceItemDto;
import com.example.tallyintegration.dto.TaxDto;
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

    private static final String TALLY_URL = "http://localhost:9000";
    private static final String DEFAULT_UNIT = "Nos";
    private static final BigDecimal TOLERANCE = new BigDecimal("0.01");
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private final RestClient restClient = RestClient.create();

    public String createPurchaseInvoice(PurchaseInvoiceDto dto, Logger logger) {
        validateInvoice(dto);

        BigDecimal roundOff = dto.getRoundOff() == null ? BigDecimal.ZERO : dto.getRoundOff();

        BigDecimal subtotal = dto.getItems().stream()
                .map(this::lineAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal taxTotal = dto.getTaxes().stream()
                .map(TaxDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expected = subtotal.add(taxTotal).add(roundOff);
        if (expected.subtract(dto.getGrandTotal()).abs().compareTo(TOLERANCE) > 0) {
            throw new IllegalArgumentException("Total mismatch: calculated "
                    + expected + " but received " + dto.getGrandTotal());
        }

        // Bill reference in Tally: supplier's invoice number if given, else our invoice number.
        String billRef = hasText(dto.getSupplierInvoiceNumber())
                ? dto.getSupplierInvoiceNumber().trim()
                : dto.getInvoiceNumber().trim();

        String xml = TallyXmlConstants.TALLY_PURCHASE_INVOICE
                .replace("{companyName}", escapeXml(dto.getCompanyName()))
                .replace("{invoiceDate}", formatDate(dto.getInvoiceDate()))
                .replace("{invoiceNumber}", escapeXml(dto.getInvoiceNumber()))
                .replace("{billRef}", escapeXml(billRef))
                .replace("{supplierLedger}", escapeXml(dto.getSupplierLedger()))
                .replace("{placeOfSupply}", escapeXml(dto.getPlaceOfSupply()))
                .replace("{referenceTags}", buildReferenceTags(dto))
                .replace("{supplierStateTags}", buildSupplierStateTags(dto))
                .replace("{companyStateTags}", buildCompanyStateTags(dto))
                .replace("{partyGstTags}", buildPartyGstTags(dto))
                .replace("{inventoryEntries}", buildInventoryEntries(dto))
                .replace("{taxEntries}", buildTaxEntries(dto, roundOff))
                .replace("{totalAmount}", dto.getGrandTotal()
                        .setScale(2, RoundingMode.HALF_UP).toPlainString());

        logger.info("Subtotal: {}, Tax: {}, RoundOff: {}, Total: {}",
                subtotal, taxTotal, roundOff, dto.getGrandTotal());
        logger.info("Sending Purchase Invoice XML to Tally:\n{}", xml);

        return restClient.post()
                .uri(TALLY_URL)
                .contentType(MediaType.APPLICATION_XML)
                .body(xml)
                .retrieve()
                .body(String.class);
    }

    private BigDecimal lineAmount(SalesInvoiceItemDto item) {
        return item.getQuantity().multiply(item.getRate())
                .setScale(2, RoundingMode.HALF_UP);
    }

    /** Supplier's own bill number and date (verify tag names against a Tally export). */
    private String buildReferenceTags(PurchaseInvoiceDto dto) {
        StringBuilder sb = new StringBuilder();
        if (hasText(dto.getSupplierInvoiceNumber())) {
            sb.append("<REFERENCE>").append(escapeXml(dto.getSupplierInvoiceNumber().trim())).append("</REFERENCE>");
        }
        if (hasText(dto.getSupplierInvoiceDate())) {
            sb.append("<REFERENCEDATE>").append(formatDate(dto.getSupplierInvoiceDate())).append("</REFERENCEDATE>");
        }
        return sb.toString();
    }

    /** STATENAME on a purchase voucher is the supplier's state. */
    private String buildSupplierStateTags(PurchaseInvoiceDto dto) {
        if (!hasText(dto.getSupplierState())) return "";
        return "<STATENAME>" + escapeXml(dto.getSupplierState().trim()) + "</STATENAME>";
    }

    private String buildCompanyStateTags(PurchaseInvoiceDto dto) {
        if (!hasText(dto.getCompanyState())) return "";
        return "<CMPGSTSTATE>" + escapeXml(dto.getCompanyState().trim()) + "</CMPGSTSTATE>";
    }

    private String buildPartyGstTags(PurchaseInvoiceDto dto) {
        if (!hasText(dto.getSupplierGstin())) {
            return "<GSTREGISTRATIONTYPE>Unregistered/Consumer</GSTREGISTRATIONTYPE>";
        }
        return "<GSTREGISTRATIONTYPE>Regular</GSTREGISTRATIONTYPE>"
                + "<PARTYGSTIN>" + escapeXml(dto.getSupplierGstin().trim()) + "</PARTYGSTIN>";
    }

    /**
     * Purchase: items are debits (ISDEEMEDPOSITIVE=Yes, negative amount).
     * GST rate and HSN are taken by Tally from the stock item master.
     */
    private String buildInventoryEntries(PurchaseInvoiceDto dto) {
        StringBuilder sb = new StringBuilder();

        for (SalesInvoiceItemDto item : dto.getItems()) {
            String unit = hasText(item.getUom()) ? item.getUom().trim() : DEFAULT_UNIT;
            String amount = lineAmount(item).toPlainString();
            String qty = item.getQuantity().stripTrailingZeros().toPlainString() + " " + unit;

            sb.append("<ALLINVENTORYENTRIES.LIST>")
                    .append("<STOCKITEMNAME>").append(escapeXml(item.getItemName())).append("</STOCKITEMNAME>")
                    .append("<GSTOVRDNTYPEOFSUPPLY>Goods</GSTOVRDNTYPEOFSUPPLY>")
                    .append("<GSTRATEINFERAPPLICABILITY>As per Masters/Company</GSTRATEINFERAPPLICABILITY>")
                    .append("<GSTHSNINFERAPPLICABILITY>As per Masters/Company</GSTHSNINFERAPPLICABILITY>")
                    .append("<ISDEEMEDPOSITIVE>Yes</ISDEEMEDPOSITIVE>")
                    .append("<RATE>").append(item.getRate().toPlainString()).append("/").append(unit).append("</RATE>")
                    .append("<AMOUNT>-").append(amount).append("</AMOUNT>")
                    .append("<ACTUALQTY>").append(qty).append("</ACTUALQTY>")
                    .append("<BILLEDQTY>").append(qty).append("</BILLEDQTY>")
                    .append("<BATCHALLOCATIONS.LIST>")
                    .append("<AMOUNT>-").append(amount).append("</AMOUNT>")
                    .append("<ACTUALQTY>").append(qty).append("</ACTUALQTY>")
                    .append("<BILLEDQTY>").append(qty).append("</BILLEDQTY>")
                    .append("</BATCHALLOCATIONS.LIST>")
                    .append("<ACCOUNTINGALLOCATIONS.LIST>")
                    .append("<LEDGERNAME>").append(escapeXml(dto.getPurchaseLedger())).append("</LEDGERNAME>")
                    .append("<ISDEEMEDPOSITIVE>Yes</ISDEEMEDPOSITIVE>")
                    .append("<AMOUNT>-").append(amount).append("</AMOUNT>")
                    .append("</ACCOUNTINGALLOCATIONS.LIST>")
                    .append("</ALLINVENTORYENTRIES.LIST>");
        }
        return sb.toString();
    }

    /** Purchase: tax entries are debits (ISDEEMEDPOSITIVE=Yes, negative amount). */
    private String buildTaxEntries(PurchaseInvoiceDto dto, BigDecimal roundOff) {
        StringBuilder sb = new StringBuilder();

        for (TaxDto t : dto.getTaxes()) {
            if (t.getAmount().signum() == 0) continue;
            String taxAmount = t.getAmount().setScale(2, RoundingMode.HALF_UP).toPlainString();

            sb.append("<LEDGERENTRIES.LIST>");
            if (t.getRate() != null) {
                sb.append("<RATEOFINVOICETAX.LIST TYPE=\"Number\">")
                        .append("<RATEOFINVOICETAX>")
                        .append(t.getRate().stripTrailingZeros().toPlainString())
                        .append("</RATEOFINVOICETAX>")
                        .append("</RATEOFINVOICETAX.LIST>");
            }
            sb.append("<LEDGERNAME>").append(escapeXml(t.getLedgerName())).append("</LEDGERNAME>")
                    .append("<ISDEEMEDPOSITIVE>Yes</ISDEEMEDPOSITIVE>")
                    .append("<ISPARTYLEDGER>No</ISPARTYLEDGER>")
                    .append("<AMOUNT>-").append(taxAmount).append("</AMOUNT>")
                    .append("<VATEXPAMOUNT>-").append(taxAmount).append("</VATEXPAMOUNT>")
                    .append("</LEDGERENTRIES.LIST>");
        }

        appendRoundOffEntry(sb, roundOff);
        return sb.toString();
    }

    /**
     * Purchase side: positive round-off = debit (adds to the bill), negative = credit.
     * Debit  -> ISDEEMEDPOSITIVE=Yes, AMOUNT negative
     * Credit -> ISDEEMEDPOSITIVE=No,  AMOUNT positive
     */
    private void appendRoundOffEntry(StringBuilder sb, BigDecimal amount) {
        if (amount.signum() == 0) return;
        String deemedPositive = amount.signum() > 0 ? "Yes" : "No";
        sb.append("<LEDGERENTRIES.LIST>")
                .append("<LEDGERNAME>Round Off</LEDGERNAME>")
                .append("<ISDEEMEDPOSITIVE>").append(deemedPositive).append("</ISDEEMEDPOSITIVE>")
                .append("<ISPARTYLEDGER>No</ISPARTYLEDGER>")
                .append("<AMOUNT>").append(amount.negate().setScale(2, RoundingMode.HALF_UP).toPlainString()).append("</AMOUNT>")
                .append("</LEDGERENTRIES.LIST>");
    }

    private void validateInvoice(PurchaseInvoiceDto dto) {
        Objects.requireNonNull(dto, "Purchase invoice cannot be null");
        requireText(dto.getCompanyName(), "Company name");
        requireText(dto.getSupplierLedger(), "Supplier ledger");
        requireText(dto.getInvoiceNumber(), "Invoice number");
        requireText(dto.getInvoiceDate(), "Invoice date");
        requireText(dto.getPurchaseLedger(), "Purchase ledger");
        requireText(dto.getPlaceOfSupply(), "Place of supply");

        if (dto.getGrandTotal() == null)
            throw new IllegalArgumentException("Grand total is required");
        if (dto.getItems() == null || dto.getItems().isEmpty())
            throw new IllegalArgumentException("Purchase invoice must contain at least one item");
        if (dto.getTaxes() == null)
            throw new IllegalArgumentException("Taxes list is required (can be empty)");

        for (SalesInvoiceItemDto item : dto.getItems()) {
            if (item == null) throw new IllegalArgumentException("Invoice item cannot be null");
            requireText(item.getItemName(), "Item name");
            if (item.getQuantity() == null || item.getQuantity().signum() <= 0)
                throw new IllegalArgumentException("Quantity must be greater than zero");
            if (item.getRate() == null || item.getRate().signum() < 0)
                throw new IllegalArgumentException("Rate cannot be negative");
            if (item.getGstRate() != null && (item.getGstRate().signum() < 0
                    || item.getGstRate().compareTo(HUNDRED) > 0))
                throw new IllegalArgumentException("GST rate must be between 0 and 100");
        }

        for (TaxDto t : dto.getTaxes()) {
            if (t == null) throw new IllegalArgumentException("Tax entry cannot be null");
            requireText(t.getLedgerName(), "Tax ledger name");
            if (t.getAmount() == null || t.getAmount().signum() < 0)
                throw new IllegalArgumentException("Tax amount cannot be negative");
            if (t.getRate() != null && (t.getRate().signum() < 0
                    || t.getRate().compareTo(HUNDRED) > 0))
                throw new IllegalArgumentException("Tax rate must be between 0 and 100");
        }

        // Validate dates before sending a request to Tally.
        formatDate(dto.getInvoiceDate());
        if (hasText(dto.getSupplierInvoiceDate())) {
            formatDate(dto.getSupplierInvoiceDate());
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private void requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
    }

    private String escapeXml(String value) {
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    private String formatDate(String date) {
        // Accept yyyy-MM-dd and convert it to Tally's yyyyMMdd format.
        return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
                .format(DateTimeFormatter.BASIC_ISO_DATE);
    }
}