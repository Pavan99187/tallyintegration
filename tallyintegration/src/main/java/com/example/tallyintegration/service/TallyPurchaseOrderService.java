package com.example.tallyintegration.service;

import com.example.tallyintegration.dto.PurchaseOrderDto;
import com.example.tallyintegration.xmlconstants.TallyXmlConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TallyPurchaseOrderService {

    private static final Logger logger = LoggerFactory.getLogger(TallyPurchaseOrderService.class);

    private final String url = "http://localhost:9000";

    // TODO: move these to application.properties later
    private static final String VOUCHER_TYPE = "Purchase Order";
    private static final String PURCHASE_LEDGER = "Purchase Account";
    private static final String DEFAULT_GODOWN = "Main Store";
    private static final String BATCH_NAME = "Primary Batch";

    private static final DateTimeFormatter TALLY_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter DUE_DATE =
            DateTimeFormatter.ofPattern("d-MMM-yy", Locale.ENGLISH);
    // Tally's JD attribute = days since 31-Dec-1899 (1-Apr-26 -> 46112)
    private static final LocalDate JD_EPOCH = LocalDate.of(1899, 12, 31);
    private static final Pattern LINE_ERROR =
            Pattern.compile("<LINEERROR>(.*?)</LINEERROR>", Pattern.DOTALL);

    @Value("${companyName}")
    private String companyName;

    private final RestClient restClient;

    public TallyPurchaseOrderService(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Creates the Purchase Order in Tally.
     * Throws IllegalArgumentException for bad payload data (do not retry) and
     * IllegalStateException when Tally rejects the voucher or is unreachable.
     */
    public String createPurchaseOrder(PurchaseOrderDto dto, Logger logger) {

        validate(dto);

        // TODO next step: create missing masters (UOM, stock group, godown, items, supplier ledger)

        String orderNo = dto.erpName().trim();
        LocalDate orderDate = dto.transactionDate();
        LocalDate dueDate = dto.requiredBy() != null ? dto.requiredBy() : orderDate;

        // One XML block per PO line
        StringBuilder inventoryEntries = new StringBuilder();
        BigDecimal total = BigDecimal.ZERO;

        for (PurchaseOrderDto.Item item : dto.items()) {
            BigDecimal amount = (item.amount() != null ? item.amount() : item.qty().multiply(item.rate()))
                    .setScale(2, RoundingMode.HALF_UP);
            String godown = hasText(item.warehouse()) ? item.warehouse() : DEFAULT_GODOWN;

            inventoryEntries.append(TallyXmlConstants.TALLY_PURCHASE_ORDER_ITEM
                    .replace("{itemName}", escapeXml(item.itemCode().trim()))
                    .replace("{uom}", escapeXml(item.uom().trim()))
                    .replace("{qty}", item.qty().stripTrailingZeros().toPlainString())
                    .replace("{rate}", money(item.rate()))
                    .replace("{amount}", "-" + money(amount))   // item lines are negative
                    .replace("{godown}", escapeXml(godown))
                    .replace("{batchName}", escapeXml(BATCH_NAME))
                    .replace("{orderNumber}", escapeXml(orderNo))
                    .replace("{dueDate}", dueDate.format(DUE_DATE))
                    .replace("{dueDateJd}", String.valueOf(ChronoUnit.DAYS.between(JD_EPOCH, dueDate)))
                    .replace("{purchaseLedger}", escapeXml(PURCHASE_LEDGER)));

            total = total.add(amount);
        }

        String xml = TallyXmlConstants.TALLY_PURCHASE_ORDER
                .replace("{companyName}", escapeXml(companyName))
                .replace("{voucherType}", escapeXml(VOUCHER_TYPE))
                .replace("{orderDate}", orderDate.format(TALLY_DATE))
                .replace("{orderNumber}", escapeXml(orderNo))
                .replace("{supplierLedger}", escapeXml(dto.supplierName().trim()))
                .replace("{totalAmount}", money(total))         // party ledger is positive
                .replace("{inventoryEntries}", inventoryEntries.toString()); // keep last

        logger.info("Creating Tally Purchase Order: {}", orderNo);
       System.out.println("xml = " + xml);

        String response;
        try {
            response = restClient.post()
                    .uri(url)
                    .contentType(new MediaType("text", "xml", StandardCharsets.UTF_8))
                    .body(xml)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            throw new IllegalStateException("Tally not reachable or timed out: " + e.getMessage(), e);
        }

        logger.info("Tally response: {}", response);
        checkTallyResponse(orderNo, response);
        return response;
    }

    /** Tally returns HTTP 200 even on failure, so read the counts inside the XML. */
    private void checkTallyResponse(String orderNo, String response) {
        if (response == null || response.isBlank()) {
            throw new IllegalStateException("Empty response from Tally for Purchase Order " + orderNo);
        }

        List<String> lineErrors = new ArrayList<>();
        Matcher m = LINE_ERROR.matcher(response);
        while (m.find()) {
            lineErrors.add(m.group(1).trim());
        }

        int created = tagCount(response, "CREATED");
        int altered = tagCount(response, "ALTERED");
        int errors = tagCount(response, "ERRORS");

        if (!lineErrors.isEmpty() || errors > 0 || (created + altered) == 0) {
            String reason = lineErrors.isEmpty()
                    ? "created=" + created + ", errors=" + errors
                    : String.join("; ", lineErrors);
            throw new IllegalStateException("Tally rejected Purchase Order " + orderNo + ": " + reason);
        }
    }

    private int tagCount(String response, String tag) {
        Matcher m = Pattern.compile("<" + tag + ">\\s*(\\d+)\\s*</" + tag + ">").matcher(response);
        return m.find() ? Integer.parseInt(m.group(1)) : 0;
    }

    private void validate(PurchaseOrderDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Purchase Order payload is empty");
        }
        List<String> problems = new ArrayList<>();
        if (!hasText(dto.erpName()))
            problems.add("erp_name is missing");
        if (!hasText(dto.supplierName()))
            problems.add("supplier_name is missing");
        if (dto.transactionDate() == null)
            problems.add("transaction_date is missing");
        if (dto.items() == null || dto.items().isEmpty()) {
            problems.add("items is empty");
        } else {
            for (int n = 0; n < dto.items().size(); n++) {
                PurchaseOrderDto.Item i = dto.items().get(n);
                String at = "items[" + n + "] ";
                if (!hasText(i.itemCode())) problems.add(at + "item_code is missing");
                if (!hasText(i.uom())) problems.add(at + "uom is missing");
                if (i.qty() == null || i.qty().signum() <= 0) problems.add(at + "qty must be > 0");
                if (i.rate() == null || i.rate().signum() <= 0) problems.add(at + "rate must be > 0");
            }
        }
        if (!problems.isEmpty()) {
            throw new IllegalArgumentException(
                    "Invalid Purchase Order payload: " + String.join(", ", problems));
        }
    }

    private String money(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
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