package com.example.tallyintegration.service;

import com.example.tallyintegration.dto.LedgerDto;
import com.example.tallyintegration.dto.SalesInvoiceDto;
import com.example.tallyintegration.dto.SyncQueueItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FrappeSyncPollerService {


    private final Logger logger = LoggerFactory.getLogger(FrappeSyncPollerService.class);

    @Value("${frappeBaseUrl}")
    private String frappeBaseUrl;

    @Value("${frappeApiKey}")
    private String frappeApiKey;

    @Value("${frappeApiSecret}")
    private String frappeApiSecret;

    @Value("${getPendingUrl}")
    private String getPendingUrl;


    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final TallyLedgerService tallyLedgerService;
    private final TallySalesInvoiceService tallySalesInvoiceService;

    public FrappeSyncPollerService(RestClient restClient, ObjectMapper objectMapper, TallyLedgerService tallyLedgerService, TallySalesInvoiceService tallySalesInvoiceService) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
        this.tallyLedgerService = tallyLedgerService;
        this.tallySalesInvoiceService = tallySalesInvoiceService;
    }

    @Scheduled(fixedRate = 1000) // Poll every minute
    public void pollFrappeForSyncItems() {
        // Implement the logic to poll Frappe for sync items using the provided URLs and credentials.
        // Use restClient to make HTTP requests and objectMapper to parse JSON responses.
        // For each sync item, call tallyLedgerService.createLedger() as needed.
        SyncQueueItem.FrappeResponse<List<SyncQueueItem>> response;
        try {
            response = restClient.get()
                    .uri(getPendingUrl)
                    .header("Authorization", "token " + frappeApiKey + ":" + frappeApiSecret)
                    .retrieve()
                    .body(new ParameterizedTypeReference<SyncQueueItem.FrappeResponse<List<SyncQueueItem>>>() {});

//
            processSyncItem(response.message());

        } catch (Exception e) {
            logger.error("Error occurred while polling Frappe for sync items", e);
            return;
        }
    }

    private void processSyncItem(List<SyncQueueItem> syncItems) {
         for(SyncQueueItem item:syncItems) {
             String status = "Completed";
             String message = null;
             System.out.println("item.getType() = " + item.getType());

             if (item.getType().equals("Customer")) {
                 // Print the raw JSON payload to your console logs
                 String rawJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(item.getData());
                 logger.info("Raw JSON payload for item ID {}:\n{}", item.getId(), rawJson);

                 LedgerDto ledgerDto = objectMapper.treeToValue(item.getData(), LedgerDto.class);
                 tallyLedgerService.createLedger(ledgerDto, item.getType(), logger);
             } else if (item.getType().equals("Supplier")) {
                 // Print the raw JSON payload to your console logs
                 String rawJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(item.getData());
                 logger.info("Raw JSON payload for item ID {}:\n{}", item.getId(), rawJson);

                 LedgerDto ledgerDto = objectMapper.treeToValue(item.getData(), LedgerDto.class);
                 tallyLedgerService.createLedger(ledgerDto, item.getType(), logger);
//             } else if (item.getType().equals("Sales Invoice")) {
//                 // Handle Sales Invoice type
//                    String rawJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(item.getData());
//                    logger.info("Raw JSON payload for item ID {}:\n{}", item.getId(), rawJson);
//                    SalesInvoiceDto salesInvoiceDto = objectMapper.treeToValue(item.getData(), SalesInvoiceDto.class);
//                      tallySalesInvoiceService.createSalesInvoice(salesInvoiceDto,logger);
             } else {
                 logger.info("Item type is not Customer, Supplier, or Ledger. Skipping processing for item ID: {}", item.getId());
             }
             sendSyncStatusToFrappe(item.getId(), status, message);
         }
    }

    private void sendSyncStatusToFrappe(Long itemId, String status, String message) {
        try {
            String updateUrl = frappeBaseUrl
                    + "/api/method/pa_footwear.api.tally_sync.update_status";

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("id", itemId);
            requestBody.put("status", status);
            requestBody.put("message", message != null ? message : "");

            JsonNode response = restClient.post()
                    .uri(updateUrl)
                    .header("Authorization", "token " + frappeApiKey + ":" + frappeApiSecret)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(JsonNode.class);

            logger.info("Frappe API Response: {}",
                    objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(response));

        } catch (Exception e) {
            logger.error("Error occurred while sending sync status to Frappe for item ID: {}",
                    itemId, e);
        }
    }
}
