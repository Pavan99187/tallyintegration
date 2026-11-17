package com.example.tallyintegration.controller;


import com.example.tallyintegration.dto.*;
import com.example.tallyintegration.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tally")
public class TallyMasterSyncController {

    private static final Logger log = LoggerFactory.getLogger(TallyMasterSyncController.class);
    private final TallyLedgerService tallyLedgerService;
    private final TallySalesInvoiceService tallySalesInvoiceService;
    private final TallyPurchaseInvoiceService tallyPurchaseInvoiceService;
    private final TallyRejectionsOutService tallyRejectionsOutService;
    private final TallyGrnService tallyGrnService;

    public TallyMasterSyncController(TallyLedgerService tallyLedgerService, TallySalesInvoiceService tallySalesInvoiceService, TallyPurchaseInvoiceService tallyPurchaseInvoiceService, TallyRejectionsOutService tallyRejectionsOutService, TallyRejectionsOutService tallyRejectionsOutService1, TallyGrnService tallyGrnService) {
        this.tallyLedgerService = tallyLedgerService;
        this.tallySalesInvoiceService = tallySalesInvoiceService;
        this.tallyPurchaseInvoiceService = tallyPurchaseInvoiceService;
        this.tallyRejectionsOutService = tallyRejectionsOutService1;
        this.tallyGrnService = tallyGrnService;
    }

    // Add your endpoints here for Tally master synchronization
    @PostMapping("/createSupplier")
    public ResponseEntity<String> createSupplier(
            @RequestBody LedgerDto supplierDto) {
      Logger logger = LoggerFactory.getLogger("SupplierCreation");

        String response = tallyLedgerService.createLedger(
                supplierDto, "SUPPLIER", logger);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/createCustomer")
    public ResponseEntity<String> createCustomer(
            @RequestBody LedgerDto customerDto) {
      Logger logger =  LoggerFactory.getLogger("CustomerCreation");

        String response = tallyLedgerService.createLedger(
                customerDto, "CUSTOMER", logger);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/createSalesInvoice")
    public ResponseEntity<String> createSalesInvoice(@RequestBody SalesInvoiceDto salesInvoiceDto) {
      Logger logger = LoggerFactory.getLogger("SalesInvoiceCreation");
        logger.info("========== Starting Sales Invoice Creation =======");
      String response  =  tallySalesInvoiceService.createSalesInvoice(salesInvoiceDto,logger);
      logger.info("========== Sales Invoice Creation Completed =======");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/createPurchaseInvoice")
    public ResponseEntity<String> createPurchaseInvoice(@RequestBody PurchaseInvoiceDto purchaseInvoiceDto) {
      Logger logger = LoggerFactory.getLogger("PurchaseInvoiceCreation");
        logger.info("========== Starting Purchase Invoice Creation =======");
      String response  =  tallyPurchaseInvoiceService.createPurchaseInvoice(purchaseInvoiceDto,logger);
      logger.info("========== Purchase Invoice Creation Completed =======");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/createGrn")
    public ResponseEntity<String> createGrn(@RequestBody TallyGrnDto grnDto) {
        Logger logger = LoggerFactory.getLogger("GRNCreation");
        logger.info("========== Starting GRN Creation =======");
        String response = tallyGrnService.createGrn(grnDto, logger);
        logger.info("========== GRN Creation Completed =======");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/createRejectionsOut")
    public ResponseEntity<String> createRejectionsOut(@RequestBody TallyRejectionsOutDto rejectionsOutDto) {
        Logger logger = LoggerFactory.getLogger("RejectionsOutCreation");
        logger.info("========== Starting Rejections Out Creation =======");
        String response = tallyRejectionsOutService.createRejectionsOut(rejectionsOutDto, logger);
        logger.info("========== Rejections Out Creation Completed =======");
        return ResponseEntity.ok(response);
    }


}
